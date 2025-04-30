package com.frogcrew.frogcrew.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogcrew.frogcrew.domain.model.Invite;
import com.frogcrew.frogcrew.service.InviteService;
import com.frogcrew.frogcrew.service.dto.InviteRequestDTO;
import com.frogcrew.frogcrew.service.dto.InviteResponseDTO;
import com.frogcrew.frogcrew.service.dto.InviteValidationDTO;
import com.frogcrew.frogcrew.config.ControllerTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InviteController.class)
@Import(ControllerTestConfig.class)
public class InviteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InviteService inviteService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test Case for Use Case 14: Admin Invites Crew Member
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminInvitesCrewMember() throws Exception {
        // Arrange
        InviteRequestDTO inviteRequest = new InviteRequestDTO();
        inviteRequest.setEmails(Arrays.asList("test1@example.com", "test2@example.com"));

        InviteResponseDTO invite1 = new InviteResponseDTO();
        invite1.setEmail("test1@example.com");
        invite1.setToken("token1");
        invite1.setExpiresAt(LocalDateTime.now().plusDays(7));
        invite1.setInviteLink("http://example.com/invite/token1");

        InviteResponseDTO invite2 = new InviteResponseDTO();
        invite2.setEmail("test2@example.com");
        invite2.setToken("token2");
        invite2.setExpiresAt(LocalDateTime.now().plusDays(7));
        invite2.setInviteLink("http://example.com/invite/token2");

        List<InviteResponseDTO> invites = Arrays.asList(invite1, invite2);

        when(inviteService.sendInvites(any(InviteRequestDTO.class))).thenReturn(invites);

        // Act & Assert
        mockMvc.perform(post("/invite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inviteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Invite Success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].email").value("test1@example.com"))
                .andExpect(jsonPath("$.data[1].email").value("test2@example.com"));
    }

    /**
     * Test Case for Use Case 14: Admin Views Pending Invites
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminViewsPendingInvites() throws Exception {
        // Arrange
        InviteResponseDTO invite1 = new InviteResponseDTO();
        invite1.setEmail("pending1@example.com");
        invite1.setToken("token1");
        invite1.setExpiresAt(LocalDateTime.now().plusDays(7));
        invite1.setInviteLink("http://example.com/invite/token1");

        InviteResponseDTO invite2 = new InviteResponseDTO();
        invite2.setEmail("pending2@example.com");
        invite2.setToken("token2");
        invite2.setExpiresAt(LocalDateTime.now().plusDays(7));
        invite2.setInviteLink("http://example.com/invite/token2");

        List<InviteResponseDTO> pendingInvites = Arrays.asList(invite1, invite2);

        when(inviteService.getPendingInvites()).thenReturn(pendingInvites);

        // Act & Assert
        mockMvc.perform(get("/invite/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Pending Invites Retrieved"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].email").value("pending1@example.com"))
                .andExpect(jsonPath("$.data[1].email").value("pending2@example.com"));
    }

    /**
     * Test Case for Use Case 14: Admin Cancels Invite
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminCancelsInvite() throws Exception {
        // Arrange
        String token = "token123";
        doNothing().when(inviteService).cancelInvite(token);

        // Act & Assert
        mockMvc.perform(delete("/invite/" + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Invite Cancelled"));
    }

    /**
     * Test Case: Validate Invite Token
     * This is part of the invite flow but not explicitly listed in use cases
     */
    @Test
    public void testValidateInviteToken() throws Exception {
        // Arrange
        String token = "validtoken";
        Invite invite = new Invite();
        invite.setId(UUID.randomUUID());
        invite.setEmail("user@example.com");
        invite.setToken(token);
        invite.setExpiresAt(LocalDateTime.now().plusDays(7));

        when(inviteService.validateInvite(anyString())).thenReturn(invite);

        // Act & Assert
        mockMvc.perform(get("/invite/" + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Invitation valid"))
                .andExpect(jsonPath("$.data.token").value(token));
    }
}