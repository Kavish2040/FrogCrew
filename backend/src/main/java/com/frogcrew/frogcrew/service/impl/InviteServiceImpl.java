package com.frogcrew.frogcrew.service.impl;

import com.frogcrew.frogcrew.domain.model.Invite;
import com.frogcrew.frogcrew.domain.repo.InviteRepository;
import com.frogcrew.frogcrew.exception.ResourceNotFoundException;
import com.frogcrew.frogcrew.service.InviteService;
import com.frogcrew.frogcrew.service.MailService;
import com.frogcrew.frogcrew.service.dto.InviteRequestDTO;
import com.frogcrew.frogcrew.service.dto.InviteResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {

    private final InviteRepository inviteRepository;
    private final MailService mailService;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Override
    public List<InviteResponseDTO> sendInvites(InviteRequestDTO inviteRequest) {
        List<InviteResponseDTO> responses = new ArrayList<>();

        for (String email : inviteRequest.getEmails()) {
            // Generate a unique token
            String token = UUID.randomUUID().toString();

            // Create and save the invite
            Invite invite = new Invite(email, token);
            inviteRepository.save(invite);

            // Create response DTO
            InviteResponseDTO response = new InviteResponseDTO();
            response.setEmail(email);
            response.setToken(token);
            response.setExpiresAt(invite.getExpiresAt());
            String inviteLink = frontendUrl + "/register/crew?token=" + token;
            response.setInviteLink(inviteLink);

            responses.add(response);

            // Send the invitation email
            String subject = "Invitation to join FrogCrew";
            String body = "Hello,\n\n" +
                    "You have been invited to join FrogCrew. " +
                    "Please click the link below to register:\n\n" +
                    inviteLink + "\n\n" +
                    "This invitation will expire in 7 days.\n\n" +
                    "Best regards,\n" +
                    "The FrogCrew Team";

            mailService.sendEmail(email, subject, body);
        }

        return responses;
    }

    @Override
    public Invite validateInvite(String token) {
        Invite invite = inviteRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invite not found with token: " + token));

        if (!invite.isValid()) {
            throw new IllegalStateException("Invite is not valid. It may be expired or already used.");
        }

        return invite;
    }

    @Override
    public List<InviteResponseDTO> getPendingInvites() {
        ZonedDateTime now = ZonedDateTime.now();
        return inviteRepository.findByUsedFalseAndExpiresAtAfter(now).stream()
                .map(invite -> {
                    InviteResponseDTO dto = new InviteResponseDTO();
                    dto.setEmail(invite.getEmail());
                    dto.setToken(invite.getToken());
                    dto.setExpiresAt(invite.getExpiresAt());
                    dto.setInviteLink(frontendUrl + "/register/crew?token=" + invite.getToken());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void cancelInvite(String token) {
        Invite invite = inviteRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invite not found with token: " + token));

        // Mark as used to invalidate it
        invite.setUsed(true);
        inviteRepository.save(invite);
    }
}