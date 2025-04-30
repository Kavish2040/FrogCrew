package com.frogcrew.frogcrew.web.rest;

import com.frogcrew.frogcrew.model.ApiResponse;
import com.frogcrew.frogcrew.service.CrewListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/crewList")
@RequiredArgsConstructor
public class CrewListController {

    private final CrewListService crewListService;

    @GetMapping("/{gameId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<?>> getCrewListByGameId(@PathVariable UUID gameId) {
        return ResponseEntity.ok(ApiResponse.success(crewListService.getCrewListByGameId(gameId)));
    }

    @GetMapping("/export/{gameId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<?>> exportCrewList(@PathVariable UUID gameId) {
        return ResponseEntity.ok(ApiResponse.success(crewListService.exportCrewList(gameId)));
    }
}