package com.frogcrew.frogcrew.service;

import com.frogcrew.frogcrew.domain.model.Game;
import com.frogcrew.frogcrew.service.dto.GameDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface GameService {
    GameDTO createGame(Game game);

    GameDTO updateGame(Game game);

    GameDTO findById(UUID id);

    Game getGame(UUID id);

    void deleteGame(UUID id);

    Page<GameDTO> listUpcomingGames(Pageable pageable);

    List<GameDTO> findByScheduleId(UUID scheduleId);
}