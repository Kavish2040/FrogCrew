package com.frogcrew.frogcrew.service;

import com.frogcrew.frogcrew.domain.model.Game;
import com.frogcrew.frogcrew.domain.model.GameSchedule;
import com.frogcrew.frogcrew.service.dto.CrewListDTO;

import java.util.UUID;

public interface CrewListService {
    CrewListDTO generateCrewList(Game game);

    CrewListDTO generateCrewList(GameSchedule schedule);

    void sendCrewListEmail(CrewListDTO crewList);

    void sendCrewListEmail(Game game);

    void sendCrewListEmail(GameSchedule schedule);

    CrewListDTO getCrewListByGameId(UUID gameId);

    byte[] exportCrewList(UUID gameId);
}