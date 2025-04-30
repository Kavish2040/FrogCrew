package com.frogcrew.frogcrew.web.rest;

import com.frogcrew.frogcrew.model.ApiResponse;
import com.frogcrew.frogcrew.service.InviteService;
import com.frogcrew.frogcrew.service.UserService;
import com.frogcrew.frogcrew.domain.model.Invite;
import com.frogcrew.frogcrew.service.dto.CrewMemberCreateDTO;
import com.frogcrew.frogcrew.service.dto.CrewMemberUpdateDTO;
import com.frogcrew.frogcrew.service.dto.UserDTO;
import com.frogcrew.frogcrew.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({ "/crewMember" })
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final InviteService inviteService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success("Find Success", userService.listAll()));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(@PathVariable UUID userId) {
        UserDTO user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.ok(ApiResponse.notFound("Could not find user with id " + userId));
        }
        return ResponseEntity.ok(ApiResponse.success("Find Success", user));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(
            @Valid @RequestBody CrewMemberCreateDTO crewMemberDTO,
            @RequestHeader(value = "Invitation-Token", required = false) String token)
            throws URISyntaxException {

        // Validate invitation token if present
        if (token != null && !token.isEmpty()) {
            try {
                Invite invite = inviteService.validateInvite(token);

                // Check if the email matches the invited email
                if (!invite.getEmail().equals(crewMemberDTO.getEmail())) {
                    return ResponseEntity.ok(ApiResponse.badRequest(
                            "Email does not match the invitation", null));
                }

                // Mark invitation as used after successful validation
                inviteService.cancelInvite(token);
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.ok(ApiResponse.badRequest(
                        "Invalid invitation token", null));
            } catch (IllegalStateException e) {
                return ResponseEntity.ok(ApiResponse.badRequest(
                        "Invitation token has expired or already been used", null));
            }
        }

        // Convert CrewMemberCreateDTO to UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(crewMemberDTO.getFirstName());
        userDTO.setLastName(crewMemberDTO.getLastName());
        userDTO.setEmail(crewMemberDTO.getEmail());
        userDTO.setPhoneNumber(crewMemberDTO.getPhoneNumber());
        userDTO.setPassword(crewMemberDTO.getPassword());
        userDTO.setRole(com.frogcrew.frogcrew.domain.model.UserRole.valueOf(crewMemberDTO.getRole()));

        // If positions are passed as strings, we need to convert to Position objects
        // For now, we'll handle this by leaving positions empty
        // In a real implementation, you'd look up positions by name
        userDTO.setPositionIds(new ArrayList<>());

        UserDTO result = userService.createUser(userDTO);
        return ResponseEntity
                .created(new URI("/crewMember/" + result.getId()))
                .body(ApiResponse.success("Add Success", result));
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREW')")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody CrewMemberUpdateDTO crewMemberDTO) {
        // Convert CrewMemberUpdateDTO to UserDTO
        UserDTO userDTO = userService.findById(userId);
        userDTO.setFirstName(crewMemberDTO.getFirstName());
        userDTO.setLastName(crewMemberDTO.getLastName());
        userDTO.setEmail(crewMemberDTO.getEmail());
        userDTO.setPhoneNumber(crewMemberDTO.getPhoneNumber());
        userDTO.setRole(com.frogcrew.frogcrew.domain.model.UserRole.valueOf(crewMemberDTO.getRole()));

        if (crewMemberDTO.getPositionIds() != null) {
            userDTO.setPositionIds(crewMemberDTO.getPositionIds());
        }

        return ResponseEntity.ok(ApiResponse.success("Update Success", userService.updateUser(userId, userDTO)));
    }

    @PutMapping("/disable/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> disableUser(@PathVariable UUID userId) {
        userService.disableUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Disable Success", null));
    }
}