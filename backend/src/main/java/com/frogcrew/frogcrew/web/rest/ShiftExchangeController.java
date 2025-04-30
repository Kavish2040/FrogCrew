package com.frogcrew.frogcrew.web.rest;

import com.frogcrew.frogcrew.domain.model.ShiftExchange;
import com.frogcrew.frogcrew.model.ApiResponse;
import com.frogcrew.frogcrew.service.ShiftExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/scheduledGames")
@RequiredArgsConstructor
public class ShiftExchangeController {

    private final ShiftExchangeService shiftExchangeService;

    @PostMapping("/drop")
    @PreAuthorize("hasRole('CREW')")
    public ResponseEntity<ApiResponse<ShiftExchange>> requestExchange(@RequestBody ShiftExchange exchange) {
        return ResponseEntity.ok(ApiResponse.success(shiftExchangeService.requestExchange(exchange)));
    }

    @PutMapping("/pickup/{tradeId}/{userId}")
    @PreAuthorize("hasRole('CREW')")
    public ResponseEntity<ApiResponse<ShiftExchange>> acceptExchange(
            @PathVariable UUID tradeId,
            @PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.success(shiftExchangeService.acceptExchange(tradeId, userId)));
    }

    @PutMapping("/deny/{tradeId}")
    @PreAuthorize("hasRole('CREW')")
    public ResponseEntity<ApiResponse<ShiftExchange>> rejectExchange(@PathVariable UUID tradeId) {
        return ResponseEntity.ok(ApiResponse.success(shiftExchangeService.rejectExchange(tradeId)));
    }

    @PutMapping("/approve/{tradeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ShiftExchange>> approveExchange(@PathVariable UUID tradeId) {
        return ResponseEntity.ok(ApiResponse.success(shiftExchangeService.approveExchange(tradeId)));
    }

    @GetMapping("/tradeboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<List<ShiftExchange>>> getAllExchanges() {
        return ResponseEntity.ok(ApiResponse.success(shiftExchangeService.getAllExchanges()));
    }

    @GetMapping("/get/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<List<ShiftExchange>>> getUserExchanges(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.success(shiftExchangeService.getUserExchanges(userId)));
    }
}