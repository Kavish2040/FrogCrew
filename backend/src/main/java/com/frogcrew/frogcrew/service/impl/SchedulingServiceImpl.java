package com.frogcrew.frogcrew.service.impl;

import com.frogcrew.frogcrew.domain.model.*;
import com.frogcrew.frogcrew.repository.CrewAssignmentRepository;
import com.frogcrew.frogcrew.repository.GameRepository;
import com.frogcrew.frogcrew.repository.FrogCrewUserRepository;
import com.frogcrew.frogcrew.service.SchedulingService;
import com.frogcrew.frogcrew.service.dto.CrewAssignmentDTO;
import com.frogcrew.frogcrew.service.dto.CrewAssignmentUpdateDTO;
import com.frogcrew.frogcrew.service.mapper.CrewAssignmentMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SchedulingServiceImpl implements SchedulingService {

        private final CrewAssignmentRepository crewAssignmentRepository;
        private final GameRepository gameRepository;
        private final FrogCrewUserRepository userRepository;
        private final CrewAssignmentMapper crewAssignmentMapper;

        @Override
        public CrewAssignmentDTO assignCrew(UUID gameId, UUID userId, Position position, LocalTime reportTime) {
                // Find game
                Game game = gameRepository.findById(gameId)
                                .orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + gameId));

                // Find user
                FrogCrewUser user = userRepository.findById(userId)
                                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

                // Check for existing assignment
                boolean exists = crewAssignmentRepository.existsByGameAndUserAndPosition(game, user, position);
                if (exists) {
                        throw new IllegalStateException("User already assigned to this position for this game");
                }

                // Create new assignment
                CrewAssignment assignment = CrewAssignment.builder()
                                .game(game)
                                .user(user)
                                .position(position)
                                .reportTime(reportTime)
                                .adminOverride(true)
                                .payRate(position.getDefaultPayRate())
                                .totalPay(position.getDefaultPayRate())
                                .build();

                // Save the assignment
                CrewAssignment savedAssignment = crewAssignmentRepository.save(assignment);

                // Convert to DTO and return
                return mapToAssignmentDTO(savedAssignment);
        }

        @Override
        public void unassignCrew(UUID assignmentId) {
                if (!crewAssignmentRepository.existsById(assignmentId)) {
                        throw new EntityNotFoundException("Assignment not found with id: " + assignmentId);
                }
                crewAssignmentRepository.deleteById(assignmentId);
        }

        @Override
        public void removeCrewAssignment(UUID assignmentId) {
                // Delegates to the existing unassignCrew method
                unassignCrew(assignmentId);
        }

        @Override
        public List<CrewAssignmentDTO> updateCrewAssignments(List<CrewAssignmentUpdateDTO> updates) {
                // TODO: Implement bulk crew assignment updates
                List<CrewAssignmentDTO> updatedAssignments = new ArrayList<>();

                // For each update in the list, find and update the corresponding assignment
                for (CrewAssignmentUpdateDTO update : updates) {
                        // This would involve finding the assignment by ID and updating its properties
                        // For now, we'll just throw an exception
                        throw new UnsupportedOperationException("Bulk update not implemented yet");
                }

                return updatedAssignments;
        }

        @Override
        public byte[] exportCrewListToExcel(UUID gameId) {
                // Get crew assignments for the game
                List<CrewAssignmentDTO> crewAssignments = listCrewForGame(gameId);

                // Create a new Excel workbook
                try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                        XSSFSheet sheet = workbook.createSheet("Crew List");

                        // Create header row
                        XSSFRow headerRow = sheet.createRow(0);
                        String[] headers = { "Position", "Crew Member", "Report Time" };
                        for (int i = 0; i < headers.length; i++) {
                                XSSFCell cell = headerRow.createCell(i);
                                cell.setCellValue(headers[i]);
                        }

                        // Fill data rows
                        int rowNum = 1;
                        for (CrewAssignmentDTO assignment : crewAssignments) {
                                XSSFRow row = sheet.createRow(rowNum++);
                                row.createCell(0).setCellValue(assignment.getPositionName());
                                row.createCell(1).setCellValue(assignment.getUserFullName());
                                row.createCell(2).setCellValue(assignment.getReportTime().toString());
                        }

                        // Auto-size columns
                        for (int i = 0; i < headers.length; i++) {
                                sheet.autoSizeColumn(i);
                        }

                        // Write to byte array
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        workbook.write(bos);
                        return bos.toByteArray();
                } catch (IOException e) {
                        throw new RuntimeException("Failed to generate Excel file", e);
                }
        }

        @Override
        @Transactional(readOnly = true)
        public List<CrewAssignmentDTO> listCrewForGame(UUID gameId) {
                Game game = gameRepository.findById(gameId)
                                .orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + gameId));

                List<CrewAssignment> assignments = crewAssignmentRepository.findByGame(game);
                return assignments.stream()
                                .map(this::mapToAssignmentDTO)
                                .toList();
        }

        private CrewAssignmentDTO mapToAssignmentDTO(CrewAssignment assignment) {
                return CrewAssignmentDTO.builder()
                                .id(assignment.getId())
                                .gameId(assignment.getGame().getId())
                                .userId(assignment.getUser().getId())
                                .userFirstName(assignment.getUser().getFirstName())
                                .userLastName(assignment.getUser().getLastName())
                                .positionId(assignment.getPosition().getId())
                                .positionName(assignment.getPosition().getDisplayName())
                                .reportTime(assignment.getReportTime())
                                .adminOverride(assignment.isAdminOverride())
                                .build();
        }
}