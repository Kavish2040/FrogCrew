package com.frogcrew.frogcrew.web.rest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.frogcrew.frogcrew.domain.model.Availability;
import com.frogcrew.frogcrew.domain.model.AvailabilityStatus;
import com.frogcrew.frogcrew.domain.model.FrogCrewUser;
import com.frogcrew.frogcrew.domain.model.Game;
import com.frogcrew.frogcrew.service.AvailabilityService;
import com.frogcrew.frogcrew.service.UserService;
import com.frogcrew.frogcrew.service.dto.BulkAvailabilityDTO;
import com.frogcrew.frogcrew.config.ControllerTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AvailabilityController.class)
@Import(ControllerTestConfig.class)
public class AvailabilityControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private AvailabilityService availabilityService;

        @MockBean
        private UserService userService;

        @Autowired
        private ObjectMapper objectMapper;

        private static class AvailabilitySerializer extends JsonSerializer<Availability> {
                @Override
                public void serialize(Availability availability, JsonGenerator jsonGenerator,
                                SerializerProvider serializerProvider) throws IOException {
                        jsonGenerator.writeStartObject();

                        // Include all regular fields from Availability
                        if (availability.getId() != null) {
                                jsonGenerator.writeStringField("id", availability.getId().toString());
                        }

                        // Add userId explicitly
                        if (availability.getUser() != null && availability.getUser().getId() != null) {
                                jsonGenerator.writeStringField("userId", availability.getUser().getId().toString());
                        }

                        // Add gameId explicitly
                        if (availability.getGame() != null && availability.getGame().getId() != null) {
                                jsonGenerator.writeStringField("gameId", availability.getGame().getId().toString());
                        }

                        // Add remaining fields
                        if (availability.getStatus() != null) {
                                jsonGenerator.writeStringField("status", availability.getStatus().toString());
                        }

                        if (availability.getDate() != null) {
                                jsonGenerator.writeStringField("date", availability.getDate().toString());
                        }

                        if (availability.getNotes() != null) {
                                jsonGenerator.writeStringField("notes", availability.getNotes());
                        }

                        jsonGenerator.writeBooleanField("active", availability.isActive());

                        jsonGenerator.writeEndObject();
                }
        }

        @BeforeEach
        public void setup() {
                // Register custom serializer for Availability
                SimpleModule module = new SimpleModule();
                module.addSerializer(Availability.class, new AvailabilitySerializer());
                objectMapper.registerModule(module);
        }

        /**
         * Test helper class to simulate the JSON output expected in tests
         */
        private static class TestAvailabilityDTO extends Availability {
                @Override
                public UUID getGameId() {
                        if (getGame() != null) {
                                return getGame().getId();
                        }
                        return null;
                }
        }

        /**
         * Test Case for Use Case 7: Crew Member Submits Availability
         */
        @Test
        @WithMockUser(roles = "CREW")
        public void testCrewMemberSubmitsAvailability() throws Exception {
                // Arrange
                UUID userId = UUID.randomUUID();
                UUID gameId = UUID.randomUUID();

                // Create entities
                FrogCrewUser user = FrogCrewUser.builder().id(userId).build();
                Game game = Game.builder().id(gameId).build();

                Availability availability = Mockito.mock(Availability.class);
                when(availability.getId()).thenReturn(UUID.randomUUID());
                when(availability.getUser()).thenReturn(user);
                when(availability.getGame()).thenReturn(game);
                when(availability.getUserId()).thenReturn(userId);
                when(availability.getGameId()).thenReturn(gameId); // Mock the getGameId() method
                when(availability.getStatus()).thenReturn(AvailabilityStatus.AVAILABLE);
                when(availability.getDate()).thenReturn(LocalDate.now());
                when(availability.getNotes()).thenReturn("Available all day");
                when(availability.getSubmittedAt()).thenReturn(ZonedDateTime.now());
                when(availability.getLastModifiedAt()).thenReturn(ZonedDateTime.now());
                when(availability.isActive()).thenReturn(true);

                when(availabilityService.submitAvailability(any(Availability.class))).thenReturn(availability);

                // Create a simple DTO for the request
                Availability requestAvailability = Availability.builder()
                                .user(user)
                                .game(game)
                                .status(AvailabilityStatus.AVAILABLE)
                                .notes("Available all day")
                                .build();

                // Act & Assert
                mockMvc.perform(post("/availability")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestAvailability)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.flag").value(true))
                                .andExpect(jsonPath("$.message").value("Add Success"))
                                .andExpect(jsonPath("$.data.userId").value(userId.toString()))
                                .andExpect(jsonPath("$.data.gameId").value(gameId.toString()))
                                .andExpect(jsonPath("$.data.status").value("AVAILABLE"));
        }

        /**
         * Test Case for Use Case 7: Crew Member Submits Bulk Availability
         */
        @Test
        @WithMockUser(roles = "CREW")
        public void testCrewMemberSubmitsBulkAvailability() throws Exception {
                // Arrange
                UUID userId = UUID.randomUUID();
                UUID gameId1 = UUID.randomUUID();
                UUID gameId2 = UUID.randomUUID();
                LocalDate today = LocalDate.now();
                LocalDate tomorrow = LocalDate.now().plusDays(1);

                // Create entities
                FrogCrewUser user = FrogCrewUser.builder().id(userId).build();
                Game game1 = Game.builder().id(gameId1).build();
                Game game2 = Game.builder().id(gameId2).build();

                // Create availability DTOs
                BulkAvailabilityDTO.AvailabilityDateDTO availabilityDate1 = new BulkAvailabilityDTO.AvailabilityDateDTO();
                availabilityDate1.setDate(today);
                availabilityDate1.setStatus(AvailabilityStatus.AVAILABLE);
                availabilityDate1.setGameId(gameId1);
                availabilityDate1.setNotes("Available for game 1");

                BulkAvailabilityDTO.AvailabilityDateDTO availabilityDate2 = new BulkAvailabilityDTO.AvailabilityDateDTO();
                availabilityDate2.setDate(tomorrow);
                availabilityDate2.setStatus(AvailabilityStatus.AVAILABLE);
                availabilityDate2.setGameId(gameId2);
                availabilityDate2.setNotes("Available for game 2");

                BulkAvailabilityDTO bulkAvailabilityDTO = new BulkAvailabilityDTO();
                bulkAvailabilityDTO.setUserId(userId);
                bulkAvailabilityDTO.setAvailabilities(Arrays.asList(availabilityDate1, availabilityDate2));

                Availability availability1 = Availability.builder()
                                .id(UUID.randomUUID())
                                .user(user)
                                .game(game1)
                                .date(today)
                                .status(AvailabilityStatus.AVAILABLE)
                                .notes("Available for game 1")
                                .submittedAt(ZonedDateTime.now())
                                .lastModifiedAt(ZonedDateTime.now())
                                .active(true)
                                .build();

                Availability availability2 = Availability.builder()
                                .id(UUID.randomUUID())
                                .user(user)
                                .game(game2)
                                .date(tomorrow)
                                .status(AvailabilityStatus.AVAILABLE)
                                .notes("Available for game 2")
                                .submittedAt(ZonedDateTime.now())
                                .lastModifiedAt(ZonedDateTime.now())
                                .active(true)
                                .build();

                List<Availability> availabilities = Arrays.asList(availability1, availability2);

                when(availabilityService.submitBulkAvailability(any(BulkAvailabilityDTO.class)))
                                .thenReturn(availabilities);

                // Act & Assert
                mockMvc.perform(post("/availability/bulk")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bulkAvailabilityDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.flag").value(true))
                                .andExpect(jsonPath("$.message").value("Bulk Add Success"))
                                .andExpect(jsonPath("$.data").isArray())
                                .andExpect(jsonPath("$.data.length()").value(2))
                                .andExpect(jsonPath("$.data[0].userId").value(userId.toString()))
                                .andExpect(jsonPath("$.data[1].userId").value(userId.toString()));
        }

        /**
         * Test Case for Use Case 7: Get User Availability for a Schedule
         */
        @Test
        @WithMockUser(roles = "CREW")
        public void testGetUserAvailabilityBySchedule() throws Exception {
                // Arrange
                UUID userId = UUID.randomUUID();
                UUID scheduleId = UUID.randomUUID();
                UUID gameId1 = UUID.randomUUID();
                UUID gameId2 = UUID.randomUUID();

                // Create entities
                FrogCrewUser user = FrogCrewUser.builder().id(userId).build();
                Game game1 = Game.builder().id(gameId1).build();
                Game game2 = Game.builder().id(gameId2).build();

                Availability availability1 = Availability.builder()
                                .id(UUID.randomUUID())
                                .user(user)
                                .game(game1)
                                .date(LocalDate.now())
                                .status(AvailabilityStatus.AVAILABLE)
                                .submittedAt(ZonedDateTime.now())
                                .lastModifiedAt(ZonedDateTime.now())
                                .active(true)
                                .build();

                Availability availability2 = Availability.builder()
                                .id(UUID.randomUUID())
                                .user(user)
                                .game(game2)
                                .date(LocalDate.now().plusDays(1))
                                .status(AvailabilityStatus.UNAVAILABLE)
                                .submittedAt(ZonedDateTime.now())
                                .lastModifiedAt(ZonedDateTime.now())
                                .active(true)
                                .build();

                List<Availability> availabilities = Arrays.asList(availability1, availability2);

                when(availabilityService.getUserAvailabilityBySchedule(userId, scheduleId)).thenReturn(availabilities);

                // Act & Assert
                mockMvc.perform(get("/availability/" + userId + "/schedule/" + scheduleId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.flag").value(true))
                                .andExpect(jsonPath("$.message").value("Find Success"))
                                .andExpect(jsonPath("$.data").isArray())
                                .andExpect(jsonPath("$.data.length()").value(2))
                                .andExpect(jsonPath("$.data[0].userId").value(userId.toString()))
                                .andExpect(jsonPath("$.data[0].status").value("AVAILABLE"))
                                .andExpect(jsonPath("$.data[1].status").value("UNAVAILABLE"));
        }
}