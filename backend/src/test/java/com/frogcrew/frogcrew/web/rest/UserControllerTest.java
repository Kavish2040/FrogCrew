package com.frogcrew.frogcrew.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogcrew.frogcrew.domain.model.UserRole;
import com.frogcrew.frogcrew.service.UserService;
import com.frogcrew.frogcrew.service.dto.CrewMemberCreateDTO;
import com.frogcrew.frogcrew.service.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.frogcrew.frogcrew.config.ControllerTestConfig;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(ControllerTestConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test Case for Use Case 1: Crew Member Creates Crew Member Profile
     */
    @Test
    @WithMockUser(roles = "CREW")
    public void testCreateCrewMemberProfile() throws Exception {
        // Arrange
        CrewMemberCreateDTO createDTO = new CrewMemberCreateDTO();
        createDTO.setFirstName("John");
        createDTO.setLastName("Doe");
        createDTO.setEmail("john.doe@example.com");
        createDTO.setPhoneNumber("123-456-7890");
        createDTO.setPassword("password123");
        createDTO.setRole("CREW");

        UserDTO responseDTO = new UserDTO();
        responseDTO.setId(UUID.randomUUID());
        responseDTO.setFirstName("John");
        responseDTO.setLastName("Doe");
        responseDTO.setEmail("john.doe@example.com");
        responseDTO.setPhoneNumber("123-456-7890");
        responseDTO.setRole(UserRole.CREW);
        responseDTO.setPositionIds(new ArrayList<>());

        when(userService.createUser(any(UserDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/crewMember")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.firstName").value("John"))
                .andExpect(jsonPath("$.data.lastName").value("Doe"))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
    }

    /**
     * Test Case for Use Case 3: Crew Member Views A Crew Member's Profile
     */
    @Test
    @WithMockUser(roles = "CREW")
    public void testViewCrewMemberProfile() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setFirstName("Jane");
        userDTO.setLastName("Smith");
        userDTO.setEmail("jane.smith@example.com");
        userDTO.setPhoneNumber("987-654-3210");
        userDTO.setRole(UserRole.CREW);
        userDTO.setPositionIds(Arrays.asList(UUID.randomUUID()));

        when(userService.findById(eq(userId))).thenReturn(userDTO);

        // Act & Assert
        mockMvc.perform(get("/crewMember/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data.firstName").value("Jane"))
                .andExpect(jsonPath("$.data.lastName").value("Smith"))
                .andExpect(jsonPath("$.data.email").value("jane.smith@example.com"));
    }

    /**
     * Test Case for Use Case 6: Crew Member Views Crew List
     */
    @Test
    @WithMockUser(roles = "CREW")
    public void testViewCrewList() throws Exception {
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

        when(userService.listAll()).thenReturn(crewMembers);

        // Act & Assert
        mockMvc.perform(get("/crewMember"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].firstName").value("Alice"))
                .andExpect(jsonPath("$.data[1].firstName").value("Bob"));
    }
}