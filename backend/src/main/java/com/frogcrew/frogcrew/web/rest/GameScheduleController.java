package com.frogcrew.frogcrew.web.rest;

import com.frogcrew.frogcrew.domain.model.GameSchedule;
import com.frogcrew.frogcrew.model.ApiResponse;
import com.frogcrew.frogcrew.service.GameScheduleService;
import com.frogcrew.frogcrew.service.dto.GameScheduleDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/gameSchedule")
@RequiredArgsConstructor
public class GameScheduleController {

    private final GameScheduleService gameScheduleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GameScheduleDTO>> addGameSchedule(@Valid @RequestBody GameScheduleDTO schedule) {
        try {
            GameSchedule result = gameScheduleService.createSchedule(mapToEntity(schedule));
            return ResponseEntity.ok(ApiResponse.success("Add Success", mapToDTO(result)));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(ApiResponse.badRequest("Provided arguments are invalid, see data for details.", null));
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<List<GameScheduleDTO>>> getAllGameSchedules() {
        List<GameScheduleDTO> schedules = gameScheduleService.getAllSchedules().stream()
                .map(this::mapToDTO)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Find Success", schedules));
    }

    @GetMapping("/{scheduleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<GameScheduleDTO>> findGameScheduleById(@PathVariable UUID scheduleId) {
        try {
            GameSchedule schedule = gameScheduleService.getSchedule(scheduleId);
            if (schedule == null) {
                return ResponseEntity.ok(ApiResponse.notFound("Could not find schedule with id " + scheduleId));
            }
            return ResponseEntity.ok(ApiResponse.success("Find Success", mapToDTO(schedule)));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.notFound("Could not find schedule with id " + scheduleId));
        }
    }

    @PutMapping("/{scheduleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GameScheduleDTO>> updateGameSchedule(
            @PathVariable UUID scheduleId,
            @Valid @RequestBody GameScheduleDTO schedule) {
        try {
            GameSchedule existing = gameScheduleService.getSchedule(scheduleId);
            if (existing == null) {
                return ResponseEntity.ok(ApiResponse.notFound("Could not find schedule with id " + scheduleId));
            }

            GameSchedule entity = mapToEntity(schedule);
            entity.setId(scheduleId);
            GameSchedule result = gameScheduleService.updateSchedule(entity);
            return ResponseEntity.ok(ApiResponse.success("Update Success", mapToDTO(result)));
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("already exists")) {
                return ResponseEntity.ok(ApiResponse.conflict(
                        "Schedule already exists for " + schedule.getSport() + " " + schedule.getSeason() + " season"));
            }
            return ResponseEntity
                    .ok(ApiResponse.badRequest("Provided arguments are invalid, see data for details.", null));
        }
    }

    @GetMapping("/season/{season}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<List<GameScheduleDTO>>> findGameSchedulesBySeason(@PathVariable String season) {
        List<GameScheduleDTO> schedules = gameScheduleService.getAllSchedules().stream()
                .filter(s -> s.getSeason().equals(season))
                .map(this::mapToDTO)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Find Success", schedules));
    }

    @PutMapping("/publish/{scheduleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> publishGameSchedule(@PathVariable UUID scheduleId) {
        try {
            GameSchedule result = gameScheduleService.publishSchedule(scheduleId);
            return ResponseEntity.ok(ApiResponse.success("Publish Success", true));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.notFound("Could not find schedule with id " + scheduleId));
        }
    }

    @DeleteMapping("/{scheduleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteGameSchedule(@PathVariable UUID scheduleId) {
        try {
            gameScheduleService.deleteSchedule(scheduleId);
            return ResponseEntity.ok(ApiResponse.success("Delete Success", null));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.notFound("Could not find schedule with id " + scheduleId));
        }
    }

    @GetMapping("/sports")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<List<String>>> findAllSports() {
        List<String> sports = gameScheduleService.getAllSchedules().stream()
                .map(GameSchedule::getSport)
                .distinct()
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Find Success", sports));
    }

    // Helper methods to map between DTO and entity
    private GameScheduleDTO mapToDTO(GameSchedule entity) {
        if (entity == null)
            return null;
        GameScheduleDTO dto = new GameScheduleDTO();
        dto.setId(entity.getId());
        dto.setSport(entity.getSport());
        dto.setSeason(entity.getSeason());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    private GameSchedule mapToEntity(GameScheduleDTO dto) {
        if (dto == null)
            return null;
        GameSchedule entity = new GameSchedule();
        entity.setId(dto.getId());
        entity.setSport(dto.getSport());
        entity.setSeason(dto.getSeason());
        entity.setStatus(dto.getStatus());
        return entity;
    }
}