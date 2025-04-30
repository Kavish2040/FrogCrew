package com.frogcrew.frogcrew.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

/**
 * DTO for {@link com.frogcrew.frogcrew.domain.model.CrewAssignment}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewAssignmentDTO {
    private UUID id;

    @NotNull(message = "Game ID is required")
    private UUID gameId;

    private String gameName;

    @NotNull(message = "User ID is required")
    private UUID userId;

    private String userName;

    @NotNull(message = "Position ID is required")
    private UUID positionId;

    private String positionName;

    @NotNull(message = "Report time is required")
    private LocalTime reportTime;

    private String userFirstName;
    private String userLastName;
    private boolean adminOverride;

    // Convenience methods
    public String getUserFullName() {
        return userFirstName + " " + userLastName;
    }
}