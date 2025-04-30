package com.frogcrew.frogcrew.service.dto;

import com.frogcrew.frogcrew.domain.model.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewAssignmentUpdateDTO {
    private UUID assignmentId;
    private UUID userId;
    private Position position;
    private LocalTime reportTime;
}