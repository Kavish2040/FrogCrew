package com.frogcrew.frogcrew.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewListDTO {
    private UUID gameId;
    private String gameStart;
    private String gameDate;
    private String venue;
    private String opponent;
    private List<CrewedUserDTO> crewedUsers;
}