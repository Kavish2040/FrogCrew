package com.frogcrew.frogcrew.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogcrew.frogcrew.service.CrewListService;
import com.frogcrew.frogcrew.service.dto.CrewListDTO;
import com.frogcrew.frogcrew.service.dto.CrewedUserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.frogcrew.frogcrew.config.ControllerTestConfig;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CrewListController.class)
@Import(ControllerTestConfig.class)
public class CrewListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrewListService crewListService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test Case for Use Case 6: Crew Member Views Crew List
     */
    @Test
    @WithMockUser(roles = "CREW")
    public void testViewCrewList() throws Exception {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        CrewedUserDTO crew1 = new CrewedUserDTO();
        crew1.setUserId(userId1);
        crew1.setGameId(gameId);
        crew1.setFullName("John Doe");
        crew1.setPosition("Camera Operator");
        crew1.setReportTime("14:00");
        crew1.setReportLocation("Press Box");

        CrewedUserDTO crew2 = new CrewedUserDTO();
        crew2.setUserId(userId2);
        crew2.setGameId(gameId);
        crew2.setFullName("Jane Smith");
        crew2.setPosition("Director");
        crew2.setReportTime("14:00");
        crew2.setReportLocation("Press Box");

        List<CrewedUserDTO> crewedUsers = new ArrayList<>();
        crewedUsers.add(crew1);
        crewedUsers.add(crew2);

        CrewListDTO crewListDTO = new CrewListDTO();
        crewListDTO.setGameId(gameId);
        crewListDTO.setGameDate("2023-09-15");
        crewListDTO.setGameStart("15:00");
        crewListDTO.setVenue("Home Stadium");
        crewListDTO.setOpponent("State University");
        crewListDTO.setCrewedUsers(crewedUsers);

        when(crewListService.getCrewListByGameId(gameId)).thenReturn(crewListDTO);

        // Act & Assert
        mockMvc.perform(get("/crewList/" + gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.data.gameDate").value("2023-09-15"))
                .andExpect(jsonPath("$.data.venue").value("Home Stadium"))
                .andExpect(jsonPath("$.data.crewedUsers").isArray())
                .andExpect(jsonPath("$.data.crewedUsers.length()").value(2))
                .andExpect(jsonPath("$.data.crewedUsers[0].fullName").value("John Doe"))
                .andExpect(jsonPath("$.data.crewedUsers[1].fullName").value("Jane Smith"));
    }

    /**
     * Test Case for Use Case 6: Export Crew List
     */
    @Test
    @WithMockUser(roles = "CREW")
    public void testExportCrewList() throws Exception {
        // Arrange
        UUID gameId = UUID.randomUUID();
        byte[] pdfData = "PDF content".getBytes();

        when(crewListService.exportCrewList(gameId)).thenReturn(pdfData);

        // Act & Assert
        mockMvc.perform(get("/crewList/export/" + gameId))
                .andExpect(status().isOk());
    }
}