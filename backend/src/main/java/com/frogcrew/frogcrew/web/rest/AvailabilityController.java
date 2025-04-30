package com.frogcrew.frogcrew.web.rest;

import com.frogcrew.frogcrew.domain.model.Availability;
import com.frogcrew.frogcrew.model.ApiResponse;
import com.frogcrew.frogcrew.service.AvailabilityService;
import com.frogcrew.frogcrew.service.UserService;
import com.frogcrew.frogcrew.service.dto.AvailabilityDTO;
import com.frogcrew.frogcrew.service.dto.BulkAvailabilityDTO;
import com.frogcrew.frogcrew.service.mapper.AvailabilityMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/availability")
@RequiredArgsConstructor
public class AvailabilityController {

        private final AvailabilityService availabilityService;
        private final UserService userService;
        private final AvailabilityMapper availabilityMapper;

        @PostMapping
        @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
        public ResponseEntity<ApiResponse<AvailabilityDTO>> addAvailability(
                        @Valid @RequestBody AvailabilityDTO availabilityDTO) {
                Availability availability = availabilityMapper.toEntity(availabilityDTO);
                Availability savedAvailability = availabilityService.submitAvailability(availability);
                return ResponseEntity.ok(ApiResponse.success("Add Success",
                                availabilityMapper.toDto(savedAvailability)));
        }

        @PostMapping("/bulk")
        @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
        public ResponseEntity<ApiResponse<List<AvailabilityDTO>>> addBulkAvailability(
                        @Valid @RequestBody BulkAvailabilityDTO bulkAvailability) {
                List<Availability> results = availabilityService.submitBulkAvailability(bulkAvailability);
                List<AvailabilityDTO> dtos = results.stream()
                                .map(availabilityMapper::toDto)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponse.success("Bulk Add Success", dtos));
        }

        @PutMapping
        @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
        public ResponseEntity<ApiResponse<AvailabilityDTO>> updateAvailability(
                        @Valid @RequestBody AvailabilityDTO availabilityDTO) {
                Availability availability = availabilityMapper.toEntity(availabilityDTO);
                Availability updatedAvailability = availabilityService.editAvailability(availability.getId(),
                                availability);
                return ResponseEntity.ok(ApiResponse.success("Update Success",
                                availabilityMapper.toDto(updatedAvailability)));
        }

        @GetMapping("/{userId}/schedule/{scheduleId}")
        @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
        public ResponseEntity<ApiResponse<List<AvailabilityDTO>>> getUserAvailabilityBySchedule(
                        @PathVariable UUID userId,
                        @PathVariable UUID scheduleId) {
                List<Availability> availabilities = availabilityService.getUserAvailabilityBySchedule(userId,
                                scheduleId);
                List<AvailabilityDTO> dtos = availabilities.stream()
                                .map(availabilityMapper::toDto)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponse.success("Find Success", dtos));
        }

        @GetMapping("/{userId}/season/{season}")
        @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
        public ResponseEntity<ApiResponse<List<AvailabilityDTO>>> getUserAvailabilityBySeason(
                        @PathVariable UUID userId,
                        @PathVariable String season) {
                List<Availability> availabilities = availabilityService.getUserAvailabilityBySeason(userId, season);
                List<AvailabilityDTO> dtos = availabilities.stream()
                                .map(availabilityMapper::toDto)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponse.success("Find Success", dtos));
        }

        @GetMapping("/user/{userId}")
        @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
        public ResponseEntity<ApiResponse<List<AvailabilityDTO>>> getUserAvailability(@PathVariable UUID userId) {
                List<Availability> availabilities = availabilityService.getUserAvailability(userId);
                List<AvailabilityDTO> dtos = availabilities.stream()
                                .map(availabilityMapper::toDto)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponse.success("Find Success", dtos));
        }

        @GetMapping("/game/{gameId}")
        @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
        public ResponseEntity<ApiResponse<List<AvailabilityDTO>>> getGameAvailability(@PathVariable UUID gameId) {
                List<Availability> availabilities = availabilityService.getGameAvailability(gameId);
                List<AvailabilityDTO> dtos = availabilities.stream()
                                .map(availabilityMapper::toDto)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponse.success("Find Success", dtos));
        }

        @GetMapping("/user/{userId}/date/{date}")
        @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
        public ResponseEntity<ApiResponse<List<AvailabilityDTO>>> getUserAvailabilityByDate(
                        @PathVariable UUID userId,
                        @PathVariable LocalDate date) {
                List<Availability> availabilities = availabilityService.getUserAvailability(userId).stream()
                                .filter(a -> a.getDate() != null && a.getDate().equals(date))
                                .collect(Collectors.toList());
                List<AvailabilityDTO> dtos = availabilities.stream()
                                .map(availabilityMapper::toDto)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponse.success("Find Success", dtos));
        }

        @GetMapping("/user/{userId}/dateRange")
        @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
        public ResponseEntity<ApiResponse<List<AvailabilityDTO>>> getUserAvailabilityByDateRange(
                        @PathVariable UUID userId,
                        @RequestParam LocalDate startDate,
                        @RequestParam LocalDate endDate) {
                List<Availability> availabilities = availabilityService.getUserAvailability(userId).stream()
                                .filter(a -> a.getDate() != null &&
                                                !a.getDate().isBefore(startDate) &&
                                                !a.getDate().isAfter(endDate))
                                .collect(Collectors.toList());
                List<AvailabilityDTO> dtos = availabilities.stream()
                                .map(availabilityMapper::toDto)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponse.success("Find Success", dtos));
        }
}