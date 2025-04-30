package com.frogcrew.frogcrew.service.dto;

import com.frogcrew.frogcrew.domain.model.AvailabilityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityDTO {
    private UUID id;
    private UUID userId;
    private UUID gameId;
    private AvailabilityStatus status;
    private Boolean availability;
    private String comment;
    private ZonedDateTime submittedAt;
    private ZonedDateTime lastModifiedAt;
    private boolean active;
    private boolean isActive;
    private LocalDate date;
    private String notes;

    // Explicit getter and setter for isActive to fix MapStruct mapping
    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}