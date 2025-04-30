package com.frogcrew.frogcrew.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogcrew.frogcrew.domain.model.GameSchedule;
import com.frogcrew.frogcrew.domain.model.ScheduleStatus;
import com.frogcrew.frogcrew.service.GameScheduleService;
import com.frogcrew.frogcrew.service.dto.GameScheduleDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.frogcrew.frogcrew.config.ControllerTestConfig;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameScheduleController.class)
@Import(ControllerTestConfig.class)
public class GameScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameScheduleService gameScheduleService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test Case for Use Case 5: Crew Member Views General Game Schedule
     */
    @Test
    @WithMockUser(roles = "CREW")
    public void testCrewMemberViewsGameSchedule() throws Exception {
        // Arrange
        List<GameSchedule> schedules = Arrays.asList(
                createGameSchedule("Football", "Fall 2023"),
                createGameSchedule("Basketball", "Winter 2023"));

        when(gameScheduleService.getAllSchedules()).thenReturn(schedules);

        // Act & Assert
        mockMvc.perform(get("/gameSchedule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].sport").value("Football"))
                .andExpect(jsonPath("$.data[1].sport").value("Basketball"));
    }

    /**
     * Test Case for Use Case 18: Admin Creates Game Schedule
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminCreatesGameSchedule() throws Exception {
        // Arrange
        GameScheduleDTO scheduleDTO = new GameScheduleDTO();
        scheduleDTO.setSport("Hockey");
        scheduleDTO.setSeason("Winter 2023");
        scheduleDTO.setStatus(ScheduleStatus.DRAFT);

        GameSchedule createdSchedule = createGameSchedule("Hockey", "Winter 2023");

        when(gameScheduleService.createSchedule(any(GameSchedule.class))).thenReturn(createdSchedule);

        // Act & Assert
        mockMvc.perform(post("/gameSchedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(scheduleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.sport").value("Hockey"))
                .andExpect(jsonPath("$.data.season").value("Winter 2023"));
    }

    /**
     * Test Case for Use Case 20: Admin Adds Games To Game Schedule
     * Since we don't have a direct API for adding games to a schedule,
     * we'll test updating a schedule which would be part of this process
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminUpdatesGameSchedule() throws Exception {
        // Arrange
        UUID scheduleId = UUID.randomUUID();

        GameScheduleDTO updateDTO = new GameScheduleDTO();
        updateDTO.setId(scheduleId);
        updateDTO.setSport("Soccer");
        updateDTO.setSeason("Spring 2023");
        updateDTO.setStatus(ScheduleStatus.DRAFT);

        GameSchedule updatedSchedule = new GameSchedule();
        updatedSchedule.setId(scheduleId);
        updatedSchedule.setSport("Soccer");
        updatedSchedule.setSeason("Spring 2023");
        updatedSchedule.setStatus(ScheduleStatus.DRAFT);

        when(gameScheduleService.getSchedule(eq(scheduleId))).thenReturn(updatedSchedule);
        when(gameScheduleService.updateSchedule(any(GameSchedule.class))).thenReturn(updatedSchedule);

        // Act & Assert
        mockMvc.perform(put("/gameSchedule/" + scheduleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.sport").value("Soccer"))
                .andExpect(jsonPath("$.data.season").value("Spring 2023"));
    }

    /**
     * Helper method to create a game schedule for testing
     */
    private GameSchedule createGameSchedule(String sport, String season) {
        GameSchedule schedule = new GameSchedule();
        schedule.setId(UUID.randomUUID());
        schedule.setSport(sport);
        schedule.setSeason(season);
        schedule.setStatus(ScheduleStatus.DRAFT);
        return schedule;
    }
}