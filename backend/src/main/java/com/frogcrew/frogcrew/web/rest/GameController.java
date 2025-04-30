package com.frogcrew.frogcrew.web.rest;

import com.frogcrew.frogcrew.domain.model.Game;
import com.frogcrew.frogcrew.domain.model.GameSchedule;
import com.frogcrew.frogcrew.model.ApiResponse;
import com.frogcrew.frogcrew.service.GameScheduleService;
import com.frogcrew.frogcrew.service.GameService;
import com.frogcrew.frogcrew.service.dto.GameCreateDTO;
import com.frogcrew.frogcrew.service.dto.GameDTO;
import com.frogcrew.frogcrew.service.dto.GameScheduleDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final GameScheduleService gameScheduleService;

    // Inner class for game schedule response
    private static class GameScheduleResponse {
        public final boolean isFinalized;
        public final List<GameDTO> games;

        public GameScheduleResponse(boolean isFinalized, List<GameDTO> games) {
            this.isFinalized = isFinalized;
            this.games = games;
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GameDTO>> addGame(@Valid @RequestBody Game game) {
        GameDTO result = gameService.createGame(game);
        return ResponseEntity.ok(ApiResponse.success("Add Success", result));
    }

    @PostMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<GameDTO>>> addGames(@Valid @RequestBody List<Game> games) {
        List<GameDTO> results = games.stream()
                .map(gameService::createGame)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Add Success", results));
    }

    @GetMapping("/{gameId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<GameDTO>> getGame(@PathVariable UUID gameId) {
        try {
            GameDTO game = gameService.findById(gameId);
            return ResponseEntity.ok(ApiResponse.success("Find Success", game));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.notFound("Could not find game with id " + gameId));
        }
    }

    @GetMapping("/schedule/{scheduleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<List<GameDTO>>> getGamesBySchedule(@PathVariable UUID scheduleId) {
        List<GameDTO> games = gameService.findByScheduleId(scheduleId);
        return ResponseEntity.ok(ApiResponse.success("Find Success", games));
    }

    @GetMapping("/upcoming")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<Page<GameDTO>>> getUpcomingGames(Pageable pageable) {
        Page<GameDTO> games = gameService.listUpcomingGames(pageable);
        return ResponseEntity.ok(ApiResponse.success("Find Success", games));
    }

    @PutMapping("/{gameId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GameDTO>> updateGame(
            @PathVariable UUID gameId,
            @Valid @RequestBody Game game) {
        try {
            game.setId(gameId);
            GameDTO result = gameService.updateGame(game);
            return ResponseEntity.ok(ApiResponse.success("Update Success", result));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.notFound("Could not find game with id " + gameId));
        }
    }

    @DeleteMapping("/{gameId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteGame(@PathVariable UUID gameId) {
        try {
            gameService.deleteGame(gameId);
            return ResponseEntity.ok(ApiResponse.success("Delete Success", null));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.notFound("Could not find game with id " + gameId));
        }
    }
}