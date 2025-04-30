package com.frogcrew.frogcrew.web.rest;

import com.frogcrew.frogcrew.domain.model.Position;
import com.frogcrew.frogcrew.domain.model.PositionProperty;
import com.frogcrew.frogcrew.model.ApiResponse;
import com.frogcrew.frogcrew.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @GetMapping("/positions")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> findAllPositions() {
        List<Map<String, Object>> positions = positionService.getAllPositions().stream()
                .map(position -> {
                    Map<String, Object> positionMap = new HashMap<>();
                    positionMap.put("id", position.getId());
                    positionMap.put("name", position.getCode());
                    positionMap.put("displayName", position.getDisplayName());
                    return positionMap;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Find Success", positions));
    }

    @PostMapping("/positions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Position>> addPosition(@RequestBody Map<String, String> request) {
        try {
            String positionName = request.get("position");
            if (positionName == null || positionName.trim().isEmpty()) {
                Map<String, String> errorDetails = new HashMap<>();
                errorDetails.put("position", "Position is required");
                ApiResponse<Map<String, String>> response = ApiResponse.badRequest(
                        "Provided arguments are invalid, see data for details.",
                        errorDetails);
                return ResponseEntity.ok((ApiResponse) response);
            }

            Position position = new Position();
            position.setCode(positionName);
            position.setDisplayName(positionName);
            position.setDefaultPayRate(new BigDecimal("0.00"));
            Position result = positionService.createPosition(position);

            return ResponseEntity.ok(ApiResponse.success("Add Success", result));
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("already exists")) {
                return ResponseEntity.ok(ApiResponse.conflict("Position already exists"));
            }
            return ResponseEntity
                    .ok(ApiResponse.badRequest("Provided arguments are invalid, see data for details.", null));
        }
    }

    @PutMapping("/positions/{positionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Position>> editPosition(
            @PathVariable UUID positionId,
            @RequestBody Map<String, String> request) {
        try {
            String positionName = request.get("position");
            if (positionName == null || positionName.trim().isEmpty()) {
                Map<String, String> errorDetails = new HashMap<>();
                errorDetails.put("position", "Position is required");
                ApiResponse<Map<String, String>> response = ApiResponse.badRequest(
                        "Provided arguments are invalid, see data for details.",
                        errorDetails);
                return ResponseEntity.ok((ApiResponse) response);
            }

            Position position = new Position();
            position.setId(positionId);
            position.setCode(positionName);
            position.setDisplayName(positionName);

            Position result = positionService.updatePosition(positionId, position);
            if (result == null) {
                return ResponseEntity.ok(ApiResponse.notFound("Could not find position with id " + positionId));
            }

            return ResponseEntity.ok(ApiResponse.success("Update Success", result));
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("already exists")) {
                return ResponseEntity.ok(ApiResponse.conflict("Position already exists"));
            }
            return ResponseEntity
                    .ok(ApiResponse.badRequest("Provided arguments are invalid, see data for details.", null));
        }
    }

    @PostMapping("/positions/properties")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PositionProperty>> addPositionProperties(
            @RequestBody PositionProperty property) {
        try {
            PositionProperty result = positionService.createPositionProperty(property);
            return ResponseEntity.ok(ApiResponse.success("Add Success", result));
        } catch (Exception e) {
            if (e.getMessage() != null) {
                if (e.getMessage().contains("not found")) {
                    return ResponseEntity.ok(
                            ApiResponse.notFound("Could not find position with id " + property.getPosition().getId()));
                } else if (e.getMessage().contains("already exist")) {
                    return ResponseEntity.ok(ApiResponse
                            .conflict("A position property already exist for game type " + property.getGameType()));
                }
            }
            return ResponseEntity
                    .ok(ApiResponse.badRequest("Provided arguments are invalid, see data for details.", null));
        }
    }

    @PutMapping("/positions/properties/{positionId}/{gameType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PositionProperty>> editPositionProperties(
            @PathVariable UUID positionId,
            @PathVariable String gameType,
            @RequestBody Map<String, Object> properties) {
        try {
            // Get the existing position
            Position position = positionService.getPositionById(positionId);
            if (position == null) {
                return ResponseEntity.ok(ApiResponse.notFound("Could not find position with id " + positionId));
            }

            PositionProperty property = new PositionProperty();
            property.setPosition(position);
            property.setGameType(gameType);
            property.setProperties(properties);

            PositionProperty result = positionService.updatePositionProperty(positionId, gameType, property);
            if (result == null) {
                return ResponseEntity.ok(ApiResponse.notFound("Could not find position properties for position with id "
                        + positionId + " and game type " + gameType));
            }

            return ResponseEntity.ok(ApiResponse.success("Update Success", result));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(ApiResponse.badRequest("Provided arguments are invalid, see data for details.", null));
        }
    }

    @GetMapping("/positions/properties/{gameType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<List<PositionProperty>>> findPositionPropertiesByGameType(
            @PathVariable String gameType) {
        try {
            List<PositionProperty> properties = positionService.getPositionPropertiesByGameType(gameType);
            if (properties.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.notFound("Could not find game type '" + gameType + "'"));
            }
            return ResponseEntity.ok(ApiResponse.success("Find Success", properties));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.notFound("Could not find game type '" + gameType + "'"));
        }
    }
}