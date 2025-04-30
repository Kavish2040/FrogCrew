package com.frogcrew.frogcrew.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogcrew.frogcrew.config.ControllerTestConfig;
import com.frogcrew.frogcrew.domain.model.UserRole;
import com.frogcrew.frogcrew.service.UserService;
import com.frogcrew.frogcrew.service.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@Import(ControllerTestConfig.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test Case for Use Case 15: Admin Deletes A Crew Member
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminDeletesCrewMember() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        doNothing().when(userService).deleteUser(userId);

        // Act & Assert
        mockMvc.perform(delete("/admin/crewMembers/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Delete Success"));
    }

    /**
     * Test Case for Use Case 16: Admin views crew members
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminViewsCrewMembers() throws Exception {
        // Arrange
        List<UserDTO> crewMembers = new ArrayList<>();

        UserDTO user1 = new UserDTO();
        user1.setId(UUID.randomUUID());
        user1.setFirstName("Alice");
        user1.setLastName("Johnson");
        user1.setEmail("alice.johnson@example.com");
        user1.setRole(UserRole.CREW);
        crewMembers.add(user1);

        UserDTO user2 = new UserDTO();
        user2.setId(UUID.randomUUID());
        user2.setFirstName("Bob");
        user2.setLastName("Williams");
        user2.setEmail("bob.williams@example.com");
        user2.setRole(UserRole.CREW);
        crewMembers.add(user2);

        UserDTO admin = new UserDTO();
        admin.setId(UUID.randomUUID());
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail("admin@example.com");
        admin.setRole(UserRole.ADMIN);
        crewMembers.add(admin);

        when(userService.listAll()).thenReturn(crewMembers);

        // Act & Assert
        mockMvc.perform(get("/admin/crewMembers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data[0].firstName").value("Alice"))
                .andExpect(jsonPath("$.data[1].firstName").value("Bob"))
                .andExpect(jsonPath("$.data[2].firstName").value("Admin"));
    }
}