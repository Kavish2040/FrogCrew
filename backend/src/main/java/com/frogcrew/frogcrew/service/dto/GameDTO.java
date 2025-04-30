package com.frogcrew.frogcrew.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private UUID gameId;
    private UUID scheduleId;
    private String gameDate;
    private String venue;
    private String opponent;
    private boolean isFinalized;
    private String gameStart;
}