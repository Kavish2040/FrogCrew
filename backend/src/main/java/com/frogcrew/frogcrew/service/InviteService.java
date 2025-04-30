package com.frogcrew.frogcrew.service;

import com.frogcrew.frogcrew.domain.model.Invite;
import com.frogcrew.frogcrew.service.dto.InviteRequestDTO;
import com.frogcrew.frogcrew.service.dto.InviteResponseDTO;

import java.util.List;

public interface InviteService {
    List<InviteResponseDTO> sendInvites(InviteRequestDTO inviteRequest);

    Invite validateInvite(String token);

    List<InviteResponseDTO> getPendingInvites();

    void cancelInvite(String token);
}