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
public class CrewedUserDTO {
    private UUID userId;
    private UUID gameId;
    private String fullName;
    private String Position;
    private String ReportTime;
    private String ReportLocation;
    private UUID crewedUserId;
}