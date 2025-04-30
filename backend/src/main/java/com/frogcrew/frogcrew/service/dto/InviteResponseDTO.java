package com.frogcrew.frogcrew.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteResponseDTO {
    private String email;
    private String token;
    private LocalDateTime expiresAt;
    private String inviteLink;
}