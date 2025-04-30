package com.frogcrew.frogcrew.web.rest;

import com.frogcrew.frogcrew.domain.model.Position;
import com.frogcrew.frogcrew.model.ApiResponse;
import com.frogcrew.frogcrew.service.GameService;
import com.frogcrew.frogcrew.service.SchedulingService;
import com.frogcrew.frogcrew.service.dto.CrewAssignmentDTO;
import com.frogcrew.frogcrew.service.dto.CrewAssignmentRemoveDTO;
import com.frogcrew.frogcrew.service.dto.CrewAssignmentRequestDTO;
import com.frogcrew.frogcrew.service.dto.CrewAssignmentUpdateDTO;
import com.frogcrew.frogcrew.service.dto.CrewListDTO;
import com.frogcrew.frogcrew.service.dto.CrewedUserDTO;
import com.frogcrew.frogcrew.service.dto.GameDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameCrewController {

        private final SchedulingService schedulingService;
        private final GameService gameService;

        @GetMapping("/crewList/{gameId}")
        public ResponseEntity<ApiResponse<CrewListDTO>> getCrewListByGameId(@PathVariable UUID gameId) {
                GameDTO game = gameService.findById(gameId);
                List<CrewAssignmentDTO> crewAssignments = schedulingService.listCrewForGame(gameId);

                // Convert CrewAssignmentDTO to CrewedUserDTO
                List<CrewedUserDTO> crewedUsers = crewAssignments.stream()
                                .map(assignment -> CrewedUserDTO.builder()
                                                .userId(assignment.getUserId())
                                                .gameId(gameId)
                                                .fullName(assignment.getUserFirstName() + " "
                                                                + assignment.getUserLastName())
                                                .Position(assignment.getPositionName())
                                                .ReportTime(assignment.getReportTime().toString())
                                                .ReportLocation(game.getVenue())
                                                .crewedUserId(assignment.getId())
                                                .build())
                                .collect(Collectors.toList());

                // Parse date and time from GameDTO
                LocalDate gameDate = LocalDate.parse(game.getGameDate());
                LocalTime gameStart = LocalTime.parse(game.getGameStart(), DateTimeFormatter.ISO_LOCAL_TIME);

                CrewListDTO crewList = CrewListDTO.builder()
                                .gameId(gameId)
                                .gameDate(game.getGameDate())
                                .venue(game.getVenue())
                                .opponent(game.getOpponent())
                                .gameStart(game.getGameStart())
                                .crewedUsers(crewedUsers)
                                .build();

                return ResponseEntity.ok(ApiResponse.success("Find Success", crewList));
        }

        @GetMapping("/crewList/export/{gameId}")
        public ResponseEntity<byte[]> exportCrewList(@PathVariable UUID gameId) {
                // Implement export functionality to XLSX
                byte[] excelData = schedulingService.exportCrewListToExcel(gameId);
                return ResponseEntity.ok()
                                .header("Content-Type",
                                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                                .header("Content-Disposition", "attachment; filename=crew-list.xlsx")
                                .body(excelData);
        }

        @GetMapping("/crewSchedule/{gameId}")
        public ResponseEntity<ApiResponse<List<CrewAssignmentDTO>>> getCrewScheduleByGameId(@PathVariable UUID gameId) {
                List<CrewAssignmentDTO> crewSchedule = schedulingService.listCrewForGame(gameId);
                return ResponseEntity.ok(ApiResponse.success("Find Success", crewSchedule));
        }

        @PostMapping("/crewSchedule/{gameId}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<CrewAssignmentDTO>> addCrewSchedule(
                        @PathVariable UUID gameId,
                        @Valid @RequestBody CrewAssignmentRequestDTO request) {
                CrewAssignmentDTO result = schedulingService.assignCrew(
                                gameId,
                                request.getUserId(),
                                request.getPosition(),
                                request.getReportTime());
                return ResponseEntity.ok(ApiResponse.success("Add Success", result));
        }

        @PutMapping("/crewSchedule")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<List<CrewAssignmentDTO>>> updateCrewSchedule(
                        @Valid @RequestBody List<CrewAssignmentUpdateDTO> updates) {
                return ResponseEntity.ok(ApiResponse.success(
                                "Update Success", schedulingService.updateCrewAssignments(updates)));
        }

        @DeleteMapping("/crewSchedule")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<Void>> deleteCrewSchedule(
                        @Valid @RequestBody CrewAssignmentRemoveDTO request) {
                schedulingService.removeCrewAssignment(request.getAssignmentId());
                return ResponseEntity.ok(ApiResponse.success("Delete Success", null));
        }
}