package com.frogcrew.frogcrew.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameCreateDTO {
    private String gameDate;
    private String venue;
    private String opponent;
    private boolean isFinalized;
}