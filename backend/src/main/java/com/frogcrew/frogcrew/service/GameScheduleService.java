package com.frogcrew.frogcrew.service;

import com.frogcrew.frogcrew.domain.model.Game;
import com.frogcrew.frogcrew.domain.model.GameSchedule;
import com.frogcrew.frogcrew.domain.model.Position;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface GameScheduleService {
    GameSchedule createSchedule(GameSchedule schedule);

    GameSchedule addGames(UUID scheduleId, List<Game> games);

    GameSchedule publishSchedule(UUID scheduleId);

    Map<Position, String> assignCrew(UUID scheduleId);

    void assignCrewToGame(UUID gameId, UUID userId, UUID positionId, boolean adminOverride);

    GameSchedule getSchedule(UUID id);

    List<GameSchedule> getAllSchedules();

    GameSchedule updateSchedule(GameSchedule schedule);

    void deleteSchedule(UUID id);
}