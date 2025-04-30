package com.frogcrew.frogcrew.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogcrew.frogcrew.domain.model.*;
import com.frogcrew.frogcrew.service.CrewScheduleService;
import com.frogcrew.frogcrew.service.dto.BulkAssignmentDTO;
import com.frogcrew.frogcrew.service.dto.CrewAssignmentDTO;
import com.frogcrew.frogcrew.service.dto.CrewAssignmentRemoveDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.frogcrew.frogcrew.config.ControllerTestConfig;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CrewScheduleController.class)
@Import(ControllerTestConfig.class)
public class CrewScheduleControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private CrewScheduleService crewScheduleService;

        @Autowired
        private ObjectMapper objectMapper;

        /**
         * Test Case for Use Case 23: Admin Schedules Crew
         */
        @Test
        @WithMockUser(roles = "ADMIN")
        public void testAdminSchedulesCrew() throws Exception {
                // Arrange
                UUID gameId = UUID.randomUUID();
                UUID userId = UUID.randomUUID();
                UUID positionId = UUID.randomUUID();

                // Create necessary entities
                Game game = Game.builder().id(gameId).build();
                FrogCrewUser user = FrogCrewUser.builder().id(userId).build();
                Position position = Position.builder().id(positionId).build();

                CrewAssignment assignment = CrewAssignment.builder()
                                .id(UUID.randomUUID())
                                .game(game)
                                .user(user)
                                .position(position)
                                .build();

                CrewAssignmentDTO resultDTO = createAssignmentDTO(assignment.getId(), gameId, userId, positionId);

                when(crewScheduleService.assignCrewMember(any(CrewAssignment.class))).thenReturn(resultDTO);

                // Act & Assert
                mockMvc.perform(post("/crewSchedule/assign")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(assignment)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.flag").value(true))
                                .andExpect(jsonPath("$.message").value("Assign Success"))
                                .andExpect(jsonPath("$.data.gameId").value(gameId.toString()))
                                .andExpect(jsonPath("$.data.userId").value(userId.toString()))
                                .andExpect(jsonPath("$.data.positionId").value(positionId.toString()));
        }

        /**
         * Test Case for Use Case 23: Admin Bulk Schedules Crew
         */
        @Test
        @WithMockUser(roles = "ADMIN")
        public void testAdminBulkSchedulesCrew() throws Exception {
                // Arrange
                UUID gameId = UUID.randomUUID();
                UUID userId1 = UUID.randomUUID();
                UUID userId2 = UUID.randomUUID();
                UUID positionId1 = UUID.randomUUID();
                UUID positionId2 = UUID.randomUUID();

                // Create entities
                Game game = Game.builder().id(gameId).build();
                FrogCrewUser user1 = FrogCrewUser.builder().id(userId1).build();
                FrogCrewUser user2 = FrogCrewUser.builder().id(userId2).build();
                Position position1 = Position.builder().id(positionId1).build();
                Position position2 = Position.builder().id(positionId2).build();

                BulkAssignmentDTO bulkAssignment = new BulkAssignmentDTO();
                bulkAssignment.setGameId(gameId);

                BulkAssignmentDTO.CrewMemberAssignmentDTO assignment1 = new BulkAssignmentDTO.CrewMemberAssignmentDTO();
                assignment1.setUserId(userId1);
                assignment1.setPositionId(positionId1);

                BulkAssignmentDTO.CrewMemberAssignmentDTO assignment2 = new BulkAssignmentDTO.CrewMemberAssignmentDTO();
                assignment2.setUserId(userId2);
                assignment2.setPositionId(positionId2);

                bulkAssignment.setAssignments(Arrays.asList(assignment1, assignment2));

                List<CrewAssignmentDTO> results = Arrays.asList(
                                createAssignmentDTO(UUID.randomUUID(), gameId, userId1, positionId1),
                                createAssignmentDTO(UUID.randomUUID(), gameId, userId2, positionId2));

                when(crewScheduleService.assignCrewMembersBulk(any(BulkAssignmentDTO.class))).thenReturn(results);

                // Act & Assert
                mockMvc.perform(post("/crewSchedule/assignBulk")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bulkAssignment)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.flag").value(true))
                                .andExpect(jsonPath("$.message").value("Bulk Assign Success"))
                                .andExpect(jsonPath("$.data").isArray())
                                .andExpect(jsonPath("$.data.length()").value(2))
                                .andExpect(jsonPath("$.data[0].gameId").value(gameId.toString()))
                                .andExpect(jsonPath("$.data[0].userId").value(userId1.toString()))
                                .andExpect(jsonPath("$.data[1].userId").value(userId2.toString()));
        }

        /**
         * Test Case for Use Case 23: Admin Views Crew Schedule by Game
         */
        @Test
        @WithMockUser(roles = "ADMIN")
        public void testViewCrewScheduleByGame() throws Exception {
                // Arrange
                UUID gameId = UUID.randomUUID();
                UUID userId1 = UUID.randomUUID();
                UUID userId2 = UUID.randomUUID();
                UUID positionId1 = UUID.randomUUID();
                UUID positionId2 = UUID.randomUUID();

                // Create entities
                Game game = Game.builder().id(gameId).build();
                FrogCrewUser user1 = FrogCrewUser.builder().id(userId1).build();
                FrogCrewUser user2 = FrogCrewUser.builder().id(userId2).build();
                Position position1 = Position.builder().id(positionId1).build();
                Position position2 = Position.builder().id(positionId2).build();

                CrewAssignment assignment1 = CrewAssignment.builder()
                                .id(UUID.randomUUID())
                                .game(game)
                                .user(user1)
                                .position(position1)
                                .build();

                CrewAssignment assignment2 = CrewAssignment.builder()
                                .id(UUID.randomUUID())
                                .game(game)
                                .user(user2)
                                .position(position2)
                                .build();

                List<CrewAssignmentDTO> assignments = Arrays.asList(
                                createAssignmentDTO(assignment1.getId(), gameId, userId1, positionId1),
                                createAssignmentDTO(assignment2.getId(), gameId, userId2, positionId2));

                when(crewScheduleService.getAssignmentsForGame(eq(gameId))).thenReturn(assignments);

                // Act & Assert
                mockMvc.perform(get("/crewSchedule/game/" + gameId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.flag").value(true))
                                .andExpect(jsonPath("$.message").value("Find Success"))
                                .andExpect(jsonPath("$.data").isArray())
                                .andExpect(jsonPath("$.data.length()").value(2))
                                .andExpect(jsonPath("$.data[0].gameId").value(gameId.toString()))
                                .andExpect(jsonPath("$.data[1].gameId").value(gameId.toString()));
        }

        /**
         * Test Case for Use Case 23: Admin Removes Crew Assignment
         */
        @Test
        @WithMockUser(roles = "ADMIN")
        public void testAdminRemovesCrewAssignment() throws Exception {
                // Arrange
                UUID assignmentId = UUID.randomUUID();

                doNothing().when(crewScheduleService).removeAssignment(any(UUID.class));

                // Act & Assert
                mockMvc.perform(delete("/crewSchedule/unassign/" + assignmentId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.flag").value(true))
                                .andExpect(jsonPath("$.message").value("Unassign Success"));
        }

        private CrewAssignmentDTO createAssignmentDTO(UUID id, UUID gameId, UUID userId, UUID positionId) {
                CrewAssignmentDTO dto = new CrewAssignmentDTO();
                dto.setId(id);
                dto.setGameId(gameId);
                dto.setUserId(userId);
                dto.setPositionId(positionId);
                return dto;
        }
}