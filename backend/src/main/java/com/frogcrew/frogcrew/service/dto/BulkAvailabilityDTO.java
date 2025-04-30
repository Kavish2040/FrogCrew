package com.frogcrew.frogcrew.service.dto;

import com.frogcrew.frogcrew.domain.model.AvailabilityStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class BulkAvailabilityDTO {
    private UUID userId;
    private List<AvailabilityDateDTO> availabilities;

    @Data
    public static class AvailabilityDateDTO {
        private LocalDate date;
        private AvailabilityStatus status;
        private String notes;
        private UUID gameId;
    }
}