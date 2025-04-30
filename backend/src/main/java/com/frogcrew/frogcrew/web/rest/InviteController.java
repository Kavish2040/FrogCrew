package com.frogcrew.frogcrew.web.rest;

import com.frogcrew.frogcrew.domain.model.Invite;
import com.frogcrew.frogcrew.model.ApiResponse;
import com.frogcrew.frogcrew.service.InviteService;
import com.frogcrew.frogcrew.service.dto.InviteRequestDTO;
import com.frogcrew.frogcrew.service.dto.InviteResponseDTO;
import com.frogcrew.frogcrew.service.dto.InviteValidationDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/invite")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<InviteResponseDTO>>> sendInvites(
            @Valid @RequestBody InviteRequestDTO inviteRequest) {
        List<InviteResponseDTO> invites = inviteService.sendInvites(inviteRequest);
        return ResponseEntity.ok(ApiResponse.success("Invite Success", invites));
    }

    @GetMapping("/{token}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> validateInvite(@PathVariable String token) {
        Invite invite = inviteService.validateInvite(token);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("token", token);
        responseData.put("email", invite.getEmail());

        return ResponseEntity.ok(ApiResponse.success("Invitation valid", responseData));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<InviteResponseDTO>>> getPendingInvites() {
        List<InviteResponseDTO> pendingInvites = inviteService.getPendingInvites();
        return ResponseEntity.ok(ApiResponse.success("Pending Invites Retrieved", pendingInvites));
    }

    @DeleteMapping("/{token}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> cancelInvite(@PathVariable String token) {
        inviteService.cancelInvite(token);
        return ResponseEntity.ok(ApiResponse.success("Invite Cancelled", null));
    }
}