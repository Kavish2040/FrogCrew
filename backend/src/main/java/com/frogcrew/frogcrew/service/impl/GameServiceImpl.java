package com.frogcrew.frogcrew.service.impl;

import com.frogcrew.frogcrew.domain.model.Game;
// import com.frogcrew.frogcrew.domain.repo.GameRepository;
import com.frogcrew.frogcrew.repository.GameRepository;
import com.frogcrew.frogcrew.service.GameService;
import com.frogcrew.frogcrew.service.dto.GameDTO;
import com.frogcrew.frogcrew.service.mapper.GameMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @Override
    public GameDTO createGame(Game game) {
        Game savedGame = gameRepository.save(game);
        return gameMapper.toDto(savedGame);
    }

    @Override
    public GameDTO updateGame(Game game) {
        final UUID gameId = game.getId();
        Game existingGame = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + gameId));

        // Preserve any fields we want to keep from existing game that weren't set in
        // the update
        if (game.getSchedule() == null) {
            game.setSchedule(existingGame.getSchedule());
        }

        Game savedGame = gameRepository.save(game);
        return gameMapper.toDto(savedGame);
    }

    @Override
    @Transactional(readOnly = true)
    public GameDTO findById(UUID id) {
        return gameRepository.findById(id)
                .map(gameMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Game getGame(UUID id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + id));
    }

    @Override
    public void deleteGame(UUID id) {
        if (!gameRepository.existsById(id)) {
            throw new EntityNotFoundException("Game not found with id: " + id);
        }
        gameRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GameDTO> listUpcomingGames(Pageable pageable) {
        ZonedDateTime now = ZonedDateTime.now();
        return gameRepository.findByEventDateTimeAfter(now, pageable)
                .map(gameMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameDTO> findByScheduleId(UUID scheduleId) {
        return gameRepository.findByScheduleId(scheduleId).stream()
                .map(gameMapper::toDto)
                .collect(Collectors.toList());
    }
}