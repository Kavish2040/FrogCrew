package com.frogcrew.frogcrew.service;

import com.frogcrew.frogcrew.domain.model.Position;
import com.frogcrew.frogcrew.service.dto.CrewAssignmentDTO;
import com.frogcrew.frogcrew.service.dto.CrewAssignmentUpdateDTO;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface SchedulingService {
    CrewAssignmentDTO assignCrew(UUID gameId, UUID userId, Position position, LocalTime reportTime);

    void unassignCrew(UUID assignmentId);

    void removeCrewAssignment(UUID assignmentId);

    List<CrewAssignmentDTO> updateCrewAssignments(List<CrewAssignmentUpdateDTO> updates);

    byte[] exportCrewListToExcel(UUID gameId);

    List<CrewAssignmentDTO> listCrewForGame(UUID gameId);
}