package com.frogcrew.frogcrew.service;

import com.frogcrew.frogcrew.domain.model.*;
import com.frogcrew.frogcrew.repository.CrewAssignmentRepository;
import com.frogcrew.frogcrew.repository.GameRepository;
import com.frogcrew.frogcrew.repository.GameScheduleRepository;
import com.frogcrew.frogcrew.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportGenerationService {

    private final CrewAssignmentRepository crewAssignmentRepository;
    private final GameRepository gameRepository;
    private final GameScheduleRepository gameScheduleRepository;
    private final PositionRepository positionRepository;

    public byte[] generateFinancialReport(UUID scheduleId) throws IOException {
        GameSchedule schedule = gameScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        List<CrewAssignment> assignments = schedule.getGames().stream()
                .flatMap(game -> crewAssignmentRepository.findByGame(game).stream())
                .collect(Collectors.toList());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), CSVFormat.DEFAULT);

        // Write header
        printer.printRecord("Game", "Date", "Crew Member", "Position", "Pay Rate", "Total Pay");

        // Write data
        for (CrewAssignment assignment : assignments) {
            printer.printRecord(
                    assignment.getGame().getDisplayName(),
                    assignment.getGame().getEventDateTime().format(DateTimeFormatter.ISO_DATE),
                    assignment.getUser().getFullName(),
                    assignment.getPosition().getDisplayName(),
                    assignment.getPayRate(),
                    assignment.getTotalPay());
        }

        printer.flush();
        return out.toByteArray();
    }

    public byte[] generatePositionReport(UUID positionId, String season) throws IOException {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        // Get games in the specified season
        List<Game> seasonGames = gameRepository.findBySeason(season);

        // Get assignments for the position in the specified season
        List<CrewAssignment> assignments = crewAssignmentRepository.findByPosition(position)
                .stream()
                .filter(a -> seasonGames.contains(a.getGame()))
                .collect(Collectors.toList());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), CSVFormat.DEFAULT);

        // Write header
        printer.printRecord("Game", "Date", "Crew Member", "Performance Rating", "Performance Notes");

        // Write data
        for (CrewAssignment assignment : assignments) {
            printer.printRecord(
                    assignment.getGame().getDisplayName(),
                    assignment.getGame().getEventDateTime().format(DateTimeFormatter.ISO_DATE),
                    assignment.getUser().getFullName(),
                    assignment.getPerformanceRating(),
                    assignment.getPerformanceNotes());
        }

        printer.flush();
        return out.toByteArray();
    }

    public byte[] generateCrewMemberReport(UUID crewMemberId) throws IOException {
        List<CrewAssignment> assignments = crewAssignmentRepository.findByUser(
                FrogCrewUser.builder().id(crewMemberId).build());

        if (assignments.isEmpty()) {
            throw new RuntimeException("Crew member not found or has no assignments");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), CSVFormat.DEFAULT);

        // Write header
        printer.printRecord("Game", "Date", "Position", "Pay Rate", "Total Pay", "Performance Rating",
                "Performance Notes");

        // Write data
        for (CrewAssignment assignment : assignments) {
            printer.printRecord(
                    assignment.getGame().getDisplayName(),
                    assignment.getGame().getEventDateTime().format(DateTimeFormatter.ISO_DATE),
                    assignment.getPosition().getDisplayName(),
                    assignment.getPayRate(),
                    assignment.getTotalPay(),
                    assignment.getPerformanceRating(),
                    assignment.getPerformanceNotes());
        }

        printer.flush();
        return out.toByteArray();
    }
}