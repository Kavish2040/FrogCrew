package com.frogcrew.frogcrew.service;

import com.frogcrew.frogcrew.domain.model.CrewAssignment;
import com.frogcrew.frogcrew.domain.model.CrewSchedule;
import com.frogcrew.frogcrew.domain.model.Position;
import com.frogcrew.frogcrew.service.dto.BulkAssignmentDTO;
import com.frogcrew.frogcrew.service.dto.CrewAssignmentDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CrewScheduleService {
    CrewSchedule createSchedule(CrewSchedule schedule);

    CrewSchedule addAssignments(UUID scheduleId, List<CrewAssignment> assignments);

    boolean publishSchedule(UUID scheduleId);

    Map<Position, String> assignCrew(UUID scheduleId);

    void assignCrewToSchedule(UUID scheduleId, UUID userId, UUID positionId, boolean adminOverride);

    CrewSchedule getSchedule(UUID id);

    List<CrewSchedule> getAllSchedules();

    CrewSchedule updateSchedule(CrewSchedule schedule);

    CrewSchedule getOrCreateCrewScheduleForGameSchedule(UUID gameScheduleId);

    List<CrewAssignmentDTO> getAssignmentsForGame(UUID gameId);

    List<CrewAssignmentDTO> getAssignmentsForUser(UUID userId);

    CrewAssignmentDTO assignCrewMember(CrewAssignment assignment);

    List<CrewAssignmentDTO> assignCrewMembersBulk(BulkAssignmentDTO bulkAssignment);

    void removeAssignment(UUID assignmentId);
}