package com.frogcrew.frogcrew.service.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BulkAssignmentDTO {
    private UUID gameId;
    private List<CrewMemberAssignmentDTO> assignments;

    @Data
    public static class CrewMemberAssignmentDTO {
        private UUID userId;
        private UUID positionId;
    }
}