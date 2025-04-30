package com.frogcrew.frogcrew.web.rest;

import com.frogcrew.frogcrew.domain.model.CrewAssignment;
import com.frogcrew.frogcrew.domain.model.CrewSchedule;
import com.frogcrew.frogcrew.model.ApiResponse;
import com.frogcrew.frogcrew.service.CrewScheduleService;
import com.frogcrew.frogcrew.service.dto.BulkAssignmentDTO;
import com.frogcrew.frogcrew.service.dto.CrewAssignmentDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/crewSchedule")
@RequiredArgsConstructor
public class CrewScheduleController {

    private final CrewScheduleService crewScheduleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CrewSchedule>> createSchedule(@RequestBody CrewSchedule schedule) {
        return ResponseEntity
                .ok(ApiResponse.success("Add Success", crewScheduleService.createSchedule(schedule)));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<CrewSchedule>> getSchedule(@PathVariable UUID scheduleId) {
        return ResponseEntity.ok(ApiResponse.success("Find Success", crewScheduleService.getSchedule(scheduleId)));
    }

    @PutMapping("/{scheduleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CrewSchedule>> updateSchedule(
            @PathVariable UUID scheduleId,
            @RequestBody CrewSchedule schedule) {
        schedule.setId(scheduleId);
        return ResponseEntity.ok(
                ApiResponse.success("Update Success", crewScheduleService.updateSchedule(schedule)));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CrewSchedule>>> getAllSchedules() {
        return ResponseEntity.ok(ApiResponse.success("Find Success", crewScheduleService.getAllSchedules()));
    }

    @PutMapping("/publish/{scheduleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> publishSchedule(@PathVariable UUID scheduleId) {
        boolean result = crewScheduleService.publishSchedule(scheduleId);
        return ResponseEntity.ok(ApiResponse.success("Publish Success", result));
    }

    @GetMapping("/game/{gameId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<List<CrewAssignmentDTO>>> getCrewAssignmentsForGame(@PathVariable UUID gameId) {
        List<CrewAssignmentDTO> assignments = crewScheduleService.getAssignmentsForGame(gameId);
        return ResponseEntity.ok(ApiResponse.success("Find Success", assignments));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<List<CrewAssignmentDTO>>> getCrewAssignmentsForUser(@PathVariable UUID userId) {
        List<CrewAssignmentDTO> assignments = crewScheduleService.getAssignmentsForUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Find Success", assignments));
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CrewAssignmentDTO>> assignCrewMember(
            @Valid @RequestBody CrewAssignment assignment) {
        CrewAssignmentDTO result = crewScheduleService.assignCrewMember(assignment);
        return ResponseEntity.ok(ApiResponse.success("Assign Success", result));
    }

    @PostMapping("/assignBulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CrewAssignmentDTO>>> assignCrewMembersBulk(
            @Valid @RequestBody BulkAssignmentDTO bulkAssignment) {
        List<CrewAssignmentDTO> results = crewScheduleService.assignCrewMembersBulk(bulkAssignment);
        return ResponseEntity.ok(ApiResponse.success("Bulk Assign Success", results));
    }

    @DeleteMapping("/unassign/{assignmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> unassignCrewMember(@PathVariable UUID assignmentId) {
        crewScheduleService.removeAssignment(assignmentId);
        return ResponseEntity.ok(ApiResponse.success("Unassign Success", null));
    }

    @GetMapping("/schedule/{scheduleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<CrewSchedule>> getCrewScheduleByGameSchedule(@PathVariable UUID scheduleId) {
        CrewSchedule crewSchedule = crewScheduleService.getOrCreateCrewScheduleForGameSchedule(scheduleId);
        return ResponseEntity.ok(ApiResponse.success("Find Success", crewSchedule));
    }
}