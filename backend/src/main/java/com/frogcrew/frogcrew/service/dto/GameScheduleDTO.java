package com.frogcrew.frogcrew.service.dto;

import com.frogcrew.frogcrew.domain.model.ScheduleStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameScheduleDTO {
    private UUID id;

    @NotBlank(message = "Sport is required")
    private String sport;

    @NotBlank(message = "Season is required")
    private String season;

    private ScheduleStatus status;
}