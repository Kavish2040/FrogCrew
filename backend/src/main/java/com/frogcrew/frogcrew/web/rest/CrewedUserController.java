package com.frogcrew.frogcrew.web.rest;

import com.frogcrew.frogcrew.domain.model.Availability;
import com.frogcrew.frogcrew.domain.model.Position;
import com.frogcrew.frogcrew.model.ApiResponse;
import com.frogcrew.frogcrew.service.AvailabilityService;
import com.frogcrew.frogcrew.service.UserService;
import com.frogcrew.frogcrew.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/CrewedUser")
@RequiredArgsConstructor
public class CrewedUserController {

        private final AvailabilityService availabilityService;
        private final UserService userService;

        @GetMapping("/{gameId}/{position}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getQualifiedAndAvailableUsers(
                        @PathVariable UUID gameId,
                        @PathVariable Position position) {
                // Get users qualified for this position
                List<UserDTO> qualifiedUsers = userService.listAll().stream()
                                .filter(user -> user.getPositionIds() != null &&
                                                user.getPositionIds().contains(position.getId()))
                                .toList();

                // Get available users for this game
                List<Availability> gameAvailabilities = availabilityService.getGameAvailability(gameId);
                List<UUID> availableUserIds = gameAvailabilities.stream()
                                .filter(a -> a.getStatus() != null && a.getStatus().toString().equals("AVAILABLE"))
                                .map(Availability::getUserId)
                                .toList();

                // Filter qualified users who are also available
                List<Map<String, Object>> qualifiedAndAvailable = new ArrayList<>();
                for (UserDTO user : qualifiedUsers) {
                        if (availableUserIds.contains(user.getId())) {
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("userId", user.getId());
                                userMap.put("fullName", user.getFirstName() + " " + user.getLastName());
                                qualifiedAndAvailable.add(userMap);
                        }
                }

                if (qualifiedAndAvailable.isEmpty()) {
                        return ResponseEntity
                                        .ok(ApiResponse.notFound("No matching crew members available for " + position));
                }

                return ResponseEntity.ok(ApiResponse.success("Find Success", qualifiedAndAvailable));
        }
}