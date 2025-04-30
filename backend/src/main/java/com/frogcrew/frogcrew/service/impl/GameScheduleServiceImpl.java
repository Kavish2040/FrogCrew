package com.frogcrew.frogcrew.service.impl;

import com.frogcrew.frogcrew.domain.model.*;
import com.frogcrew.frogcrew.repository.*;
import com.frogcrew.frogcrew.service.GameScheduleService;
import com.frogcrew.frogcrew.service.MailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameScheduleServiceImpl implements GameScheduleService {

    private final GameScheduleRepository gameScheduleRepository;
    private final GameRepository gameRepository;
    private final FrogCrewUserRepository userRepository;
    private final PositionRepository positionRepository;
    private final CrewAssignmentRepository crewAssignmentRepository;
    private final NotificationRepository notificationRepository;
    private final MailService mailService;

    @Override
    @Transactional
    public GameSchedule createSchedule(GameSchedule schedule) {
        schedule.setStatus(ScheduleStatus.DRAFT);
        schedule.setCreatedAt(ZonedDateTime.now());
        return gameScheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public GameSchedule addGames(UUID scheduleId, List<Game> games) {
        GameSchedule schedule = gameScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        games.forEach(game -> {
            game.setSchedule(schedule);
            schedule.getGames().add(game);
        });

        return gameScheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public GameSchedule publishSchedule(UUID id) {
        GameSchedule schedule = getSchedule(id);
        schedule.setStatus(ScheduleStatus.PUBLISHED);

        // Create notifications and emails for assigned crew
        schedule.getGames().forEach(game -> {
            crewAssignmentRepository.findByGame(game).forEach(assignment -> {
                // Create notification
                Notification notification = Notification.builder()
                        .user(assignment.getUser())
                        .type("SCHEDULE_ASSIGNMENT")
                        .title("New Game Assignment")
                        .body(String.format("You have been assigned to %s as %s",
                                game.getDisplayName(),
                                assignment.getPosition().getDisplayName()))
                        .read(false)
                        .createdAt(ZonedDateTime.now())
                        .build();
                notificationRepository.save(notification);

                // Queue email
                mailService.sendEmail(
                        assignment.getUser().getEmail(),
                        "New Game Assignment",
                        String.format("You have been assigned to %s as %s",
                                game.getDisplayName(),
                                assignment.getPosition().getDisplayName()));
            });
        });

        return gameScheduleRepository.save(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public GameSchedule getSchedule(UUID id) {
        return gameScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game Schedule not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameSchedule> getAllSchedules() {
        return gameScheduleRepository.findAll();
    }

    @Override
    @Transactional
    public Map<Position, String> assignCrew(UUID scheduleId) {
        GameSchedule schedule = gameScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        Map<Position, String> unresolved = new HashMap<>();

        schedule.getGames().forEach(game -> {
            CrewListTemplate template = game.getSchedule().getTemplate();
            if (template == null) {
                unresolved.put(null, "No template assigned to schedule");
                return;
            }

            template.getPositions().forEach(positionCode -> {
                Position position = positionRepository.findByCode(positionCode)
                        .orElseThrow(() -> new RuntimeException("Position not found"));

                // Find qualified users with lowest load
                List<FrogCrewUser> qualifiedUsers = findQualifiedUsers(position);
                if (qualifiedUsers.isEmpty()) {
                    unresolved.put(position,
                            String.format("No qualified users for position %s", position.getDisplayName()));
                    return;
                }

                FrogCrewUser selectedUser = selectUserWithLowestLoad(qualifiedUsers, game);
                if (selectedUser == null) {
                    unresolved.put(position,
                            String.format("No available users for position %s", position.getDisplayName()));
                    return;
                }

                // Create assignment
                CrewAssignment assignment = CrewAssignment.builder()
                        .game(game)
                        .user(selectedUser)
                        .position(position)
                        .adminOverride(false)
                        .build();
                crewAssignmentRepository.save(assignment);
            });
        });

        return unresolved;
    }

    @Override
    @Transactional
    public void assignCrewToGame(UUID gameId, UUID userId, UUID positionId, boolean adminOverride) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (game.getSchedule().getStatus() == ScheduleStatus.PUBLISHED && !adminOverride) {
            throw new RuntimeException(
                    "Cannot modify crew assignments after schedule is published without admin override");
        }

        FrogCrewUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        // Check qualifications
        if (!user.getPositions().contains(position.getCode())) {
            throw new RuntimeException("User is not qualified for this position");
        }

        // Check for overlaps
        if (hasOverlappingAssignments(user, game)) {
            throw new RuntimeException("User has overlapping assignments");
        }

        // Create or update assignment
        CrewAssignment assignment = crewAssignmentRepository.findByGameAndPosition(game, position)
                .orElse(CrewAssignment.builder()
                        .game(game)
                        .position(position)
                        .build());

        assignment.setUser(user);
        assignment.setAdminOverride(adminOverride);
        crewAssignmentRepository.save(assignment);
    }

    private List<FrogCrewUser> findQualifiedUsers(Position position) {
        // Update to use the new positions field format
        return userRepository.findAll().stream()
                .filter(user -> user.getPositions().contains(position.getCode()))
                .collect(Collectors.toList());
    }

    private FrogCrewUser selectUserWithLowestLoad(List<FrogCrewUser> users, Game game) {
        return users.stream()
                .min(Comparator.comparingLong(user -> crewAssignmentRepository.countByUserAndGame_EventDateTimeBetween(
                        user,
                        game.getEventDateTime().minusHours(24),
                        game.getEventDateTime().plusHours(24))))
                .orElse(null);
    }

    private boolean hasOverlappingAssignments(FrogCrewUser user, Game game) {
        return crewAssignmentRepository.existsByUserAndGame_EventDateTimeBetween(
                user,
                game.getEventDateTime().minusHours(24),
                game.getEventDateTime().plusHours(24));
    }

    @Override
    @Transactional
    public GameSchedule updateSchedule(GameSchedule schedule) {
        GameSchedule existingSchedule = gameScheduleRepository.findById(schedule.getId())
                .orElseThrow(() -> new EntityNotFoundException("Game Schedule not found with id: " + schedule.getId()));

        existingSchedule.setName(schedule.getName());
        existingSchedule.setSeason(schedule.getSeason());
        existingSchedule.setSport(schedule.getSport());
        existingSchedule.setStartDate(schedule.getStartDate());
        existingSchedule.setEndDate(schedule.getEndDate());
        existingSchedule.setTemplate(schedule.getTemplate());

        return gameScheduleRepository.save(existingSchedule);
    }

    @Override
    public void deleteSchedule(UUID id) {
        if (!gameScheduleRepository.existsById(id)) {
            throw new EntityNotFoundException("Schedule not found with id: " + id);
        }

        // Check if there are games associated with this schedule
        List<Game> games = gameRepository.findByScheduleId(id);
        if (!games.isEmpty()) {
            // Delete all associated games first
            for (Game game : games) {
                gameRepository.deleteById(game.getId());
            }
        }

        gameScheduleRepository.deleteById(id);
    }
}