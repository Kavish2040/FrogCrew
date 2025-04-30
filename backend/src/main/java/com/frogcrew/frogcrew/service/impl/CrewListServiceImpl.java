package com.frogcrew.frogcrew.service.impl;

import com.frogcrew.frogcrew.domain.model.*;
import com.frogcrew.frogcrew.repository.CrewAssignmentRepository;
import com.frogcrew.frogcrew.repository.GameRepository;
import com.frogcrew.frogcrew.service.CrewListService;
import com.frogcrew.frogcrew.service.MailService;
import com.frogcrew.frogcrew.service.dto.CrewAssignmentDTO;
import com.frogcrew.frogcrew.service.dto.CrewListDTO;
import com.frogcrew.frogcrew.service.dto.CrewedUserDTO;
import com.frogcrew.frogcrew.service.mapper.CrewAssignmentMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrewListServiceImpl implements CrewListService {

    private final CrewAssignmentRepository crewAssignmentRepository;
    private final CrewAssignmentMapper crewAssignmentMapper;
    private final MailService mailService;
    private final GameRepository gameRepository;

    @Override
    @Transactional(readOnly = true)
    public CrewListDTO generateCrewList(Game game) {
        List<CrewAssignment> assignments = crewAssignmentRepository.findByGame(game);
        List<CrewedUserDTO> crewedUsers = assignments.stream()
                .map(assignment -> {
                    // Convert CrewAssignmentDTO to CrewedUserDTO
                    return CrewedUserDTO.builder()
                            .userId(assignment.getUser().getId())
                            .gameId(game.getId())
                            .fullName(assignment.getUser().getFullName())
                            .Position(assignment.getPosition().getDisplayName())
                            .ReportTime(game.getEventDateTime().toLocalTime().minusMinutes(30).toString())
                            .ReportLocation(game.getVenue())
                            .crewedUserId(assignment.getId())
                            .build();
                })
                .collect(Collectors.toList());

        return CrewListDTO.builder()
                .gameId(game.getId())
                .gameDate(game.getEventDateTime().toLocalDate().toString())
                .venue(game.getVenue())
                .opponent(game.getOpponent())
                .gameStart(game.getEventDateTime().toLocalTime().toString())
                .crewedUsers(crewedUsers)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CrewListDTO generateCrewList(GameSchedule schedule) {
        // For a schedule, we'll pick the first game to represent the schedule
        // This is a simplification - in a real system you might want a different DTO
        // for schedules
        Game representativeGame = schedule.getGames().isEmpty() ? null : schedule.getGames().iterator().next();

        if (representativeGame == null) {
            throw new IllegalStateException("Cannot generate crew list for empty schedule");
        }

        List<CrewAssignment> assignments = schedule.getGames().stream()
                .flatMap(game -> crewAssignmentRepository.findByGame(game).stream())
                .collect(Collectors.toList());

        List<CrewedUserDTO> crewedUsers = assignments.stream()
                .map(assignment -> {
                    // Convert CrewAssignmentDTO to CrewedUserDTO
                    return CrewedUserDTO.builder()
                            .userId(assignment.getUser().getId())
                            .gameId(assignment.getGame().getId())
                            .fullName(assignment.getUser().getFullName())
                            .Position(assignment.getPosition().getDisplayName())
                            .ReportTime(
                                    assignment.getGame().getEventDateTime().toLocalTime().minusMinutes(30).toString())
                            .ReportLocation(assignment.getGame().getVenue())
                            .crewedUserId(assignment.getId())
                            .build();
                })
                .collect(Collectors.toList());

        return CrewListDTO.builder()
                .gameId(representativeGame.getId())
                .gameDate(representativeGame.getEventDateTime().toLocalDate().toString())
                .venue(representativeGame.getVenue())
                .opponent(representativeGame.getOpponent())
                .gameStart(representativeGame.getEventDateTime().toLocalTime().toString())
                .crewedUsers(crewedUsers)
                .build();
    }

    @Override
    @Transactional
    public void sendCrewListEmail(CrewListDTO crewList) {
        // Generate subject and body for email based on crew list data
        String subject = String.format("Crew List for Game on %s", crewList.getGameDate());
        String body = generateEmailBody(crewList);

        mailService.sendEmail(
                "crew@frogcrew.com", // TODO: Configure recipient list
                subject,
                body);
    }

    @Override
    @Transactional
    public void sendCrewListEmail(Game game) {
        CrewListDTO crewList = generateCrewList(game);
        sendCrewListEmail(crewList);
    }

    @Override
    @Transactional
    public void sendCrewListEmail(GameSchedule schedule) {
        CrewListDTO crewList = generateCrewList(schedule);
        sendCrewListEmail(crewList);
    }

    // Helper method to generate email body
    private String generateEmailBody(CrewListDTO crewList) {
        StringBuilder body = new StringBuilder();
        body.append("Crew List for Game\n\n");
        body.append("Date: ").append(crewList.getGameDate()).append("\n");
        body.append("Venue: ").append(crewList.getVenue()).append("\n");
        body.append("Opponent: ").append(crewList.getOpponent()).append("\n");
        body.append("Start Time: ").append(crewList.getGameStart()).append("\n\n");

        body.append("Assigned Crew:\n");
        for (CrewedUserDTO crew : crewList.getCrewedUsers()) {
            body.append(crew.getFullName())
                    .append(" - ")
                    .append(crew.getPosition())
                    .append(" (Report at ")
                    .append(crew.getReportTime())
                    .append(")\n");
        }

        return body.toString();
    }

    // Method to generate email body for a game
    private String generateEmailBody(Game game, List<CrewAssignment> assignments) {
        StringBuilder body = new StringBuilder();
        body.append("Crew List for Game\n\n");
        body.append("Date: ").append(game.getEventDateTime().toLocalDate()).append("\n");
        body.append("Venue: ").append(game.getVenue()).append("\n");
        body.append("Opponent: ").append(game.getOpponent()).append("\n");
        body.append("Start Time: ").append(game.getEventDateTime().toLocalTime()).append("\n\n");

        body.append("Assigned Crew:\n");
        for (CrewAssignment assignment : assignments) {
            body.append(assignment.getUser().getFullName())
                    .append(" - ")
                    .append(assignment.getPosition().getDisplayName())
                    .append(" (Report at ")
                    .append(game.getEventDateTime().toLocalTime().minusMinutes(30))
                    .append(")\n");
        }

        return body.toString();
    }

    // Method to generate email body for a schedule
    private String generateEmailBody(GameSchedule schedule, List<CrewAssignment> assignments) {
        StringBuilder body = new StringBuilder();
        body.append("Crew List for Schedule\n\n");
        body.append("Season: ").append(schedule.getSeason()).append("\n");
        body.append("Sport: ").append(schedule.getSport()).append("\n\n");

        body.append("Assigned Crew:\n");
        for (CrewAssignment assignment : assignments) {
            body.append(assignment.getUser().getFullName())
                    .append(" - ")
                    .append(assignment.getPosition().getDisplayName())
                    .append(" for game ")
                    .append(assignment.getGame().getDisplayName())
                    .append(" on ")
                    .append(assignment.getGame().getEventDateTime().toLocalDate())
                    .append("\n");
        }

        return body.toString();
    }

    @Override
    public CrewListDTO getCrewListByGameId(UUID gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + gameId));
        return generateCrewList(game);
    }

    @Override
    public byte[] exportCrewList(UUID gameId) {
        CrewListDTO crewList = getCrewListByGameId(gameId);

        // Here you would implement the actual PDF/Excel export
        // For demonstration purposes, we'll create a simple text representation
        try {
            StringBuilder content = new StringBuilder();
            content.append("Crew List for ").append(crewList.getOpponent()).append("\n\n");
            content.append("Date: ").append(crewList.getGameDate()).append("\n");
            content.append("Location: ").append(crewList.getVenue()).append("\n\n");
            content.append("Positions:\n");

            crewList.getCrewedUsers().forEach(member -> {
                content.append(member.getPosition()).append(": ");
                content.append(member.getFullName() != null
                        ? member.getFullName()
                        : "Unassigned").append("\n");
            });

            return content.toString().getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate crew list export", e);
        }
    }
}