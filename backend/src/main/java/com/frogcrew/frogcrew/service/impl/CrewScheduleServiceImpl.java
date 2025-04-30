package com.frogcrew.frogcrew.service.impl;

import com.frogcrew.frogcrew.domain.model.*;
import com.frogcrew.frogcrew.repository.*;
import com.frogcrew.frogcrew.service.CrewScheduleService;
import com.frogcrew.frogcrew.service.MailService;
import com.frogcrew.frogcrew.service.dto.BulkAssignmentDTO;
import com.frogcrew.frogcrew.service.dto.CrewAssignmentDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CrewScheduleServiceImpl implements CrewScheduleService {

    private final CrewScheduleRepository crewScheduleRepository;
    private final FrogCrewUserRepository userRepository;
    private final PositionRepository positionRepository;
    private final CrewAssignmentRepository crewAssignmentRepository;
    private final NotificationRepository notificationRepository;
    private final MailService mailService;
    private final GameScheduleRepository gameScheduleRepository;
    private final GameRepository gameRepository;

    @Override
    @Transactional
    public CrewSchedule createSchedule(CrewSchedule schedule) {
        schedule.setStatus(ScheduleStatus.DRAFT);
        schedule.setCreatedAt(ZonedDateTime.now());
        return crewScheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public CrewSchedule addAssignments(UUID scheduleId, List<CrewAssignment> assignments) {
        CrewSchedule schedule = crewScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        assignments.forEach(assignment -> {
            assignment.setCrewSchedule(schedule);
            schedule.getAssignments().add(assignment);
        });

        return crewScheduleRepository.save(schedule);
    }

    @Override
    public boolean publishSchedule(UUID scheduleId) {
        try {
            CrewSchedule schedule = crewScheduleRepository.findById(scheduleId)
                    .orElseThrow(() -> new EntityNotFoundException("Crew schedule not found with id: " + scheduleId));
            schedule.setStatus(ScheduleStatus.PUBLISHED);
            crewScheduleRepository.save(schedule);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CrewSchedule getSchedule(UUID id) {
        return crewScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Crew Schedule not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CrewSchedule> getAllSchedules() {
        return crewScheduleRepository.findAll();
    }

    @Override
    @Transactional
    public Map<Position, String> assignCrew(UUID scheduleId) {
        CrewSchedule schedule = crewScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        Map<Position, String> unresolved = new HashMap<>();

        // Find all positions from the schedule requirements
        Set<Position> requiredPositions = new HashSet<>();
        // Add code to determine required positions

        requiredPositions.forEach(position -> {
            // Find qualified users with lowest load
            List<FrogCrewUser> qualifiedUsers = findQualifiedUsers(position);
            if (qualifiedUsers.isEmpty()) {
                unresolved.put(position,
                        String.format("No qualified users for position %s", position.getDisplayName()));
                return;
            }

            FrogCrewUser selectedUser = selectUserWithLowestLoad(qualifiedUsers, schedule);
            if (selectedUser == null) {
                unresolved.put(position,
                        String.format("No available users for position %s", position.getDisplayName()));
                return;
            }

            // Create assignment
            CrewAssignment assignment = CrewAssignment.builder()
                    .crewSchedule(schedule)
                    .user(selectedUser)
                    .position(position)
                    .adminOverride(false)
                    .build();
            crewAssignmentRepository.save(assignment);
        });

        return unresolved;
    }

    @Override
    @Transactional
    public void assignCrewToSchedule(UUID scheduleId, UUID userId, UUID positionId, boolean adminOverride) {
        CrewSchedule schedule = crewScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Crew schedule not found"));

        if (schedule.getStatus() == ScheduleStatus.PUBLISHED && !adminOverride) {
            throw new RuntimeException(
                    "Cannot modify crew assignments after schedule is published without admin override");
        }

        FrogCrewUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        // Check qualifications
        if (!user.getPositions().contains(position.getCode())) {
            throw new RuntimeException("User is not qualified for this position");
        }

        // Check for overlaps
        if (hasOverlappingAssignments(user, schedule)) {
            throw new RuntimeException("User has overlapping assignments");
        }

        // Create or update assignment
        CrewAssignment assignment = crewAssignmentRepository.findByCrewScheduleAndPosition(schedule, position)
                .orElse(CrewAssignment.builder()
                        .crewSchedule(schedule)
                        .position(position)
                        .build());

        assignment.setUser(user);
        assignment.setAdminOverride(adminOverride);
        crewAssignmentRepository.save(assignment);
    }

    @Override
    @Transactional
    public CrewSchedule updateSchedule(CrewSchedule schedule) {
        CrewSchedule existingSchedule = crewScheduleRepository.findById(schedule.getId())
                .orElseThrow(() -> new EntityNotFoundException("Crew Schedule not found with id: " + schedule.getId()));

        existingSchedule.setName(schedule.getName());
        existingSchedule.setDescription(schedule.getDescription());
        existingSchedule.setStartDate(schedule.getStartDate());
        existingSchedule.setEndDate(schedule.getEndDate());
        existingSchedule.setGameSchedule(schedule.getGameSchedule());

        return crewScheduleRepository.save(existingSchedule);
    }

    @Override
    public CrewSchedule getOrCreateCrewScheduleForGameSchedule(UUID gameScheduleId) {
        // Check if a crew schedule already exists for this game schedule
        Optional<CrewSchedule> existingSchedule = crewScheduleRepository.findByGameScheduleId(gameScheduleId);

        if (existingSchedule.isPresent()) {
            return existingSchedule.get();
        }

        // If not, create a new crew schedule
        GameSchedule gameSchedule = gameScheduleRepository.findById(gameScheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Game schedule not found with id: " + gameScheduleId));

        CrewSchedule newCrewSchedule = new CrewSchedule();
        newCrewSchedule.setGameSchedule(gameSchedule);
        newCrewSchedule.setStatus(ScheduleStatus.DRAFT);

        return crewScheduleRepository.save(newCrewSchedule);
    }

    @Override
    public List<CrewAssignmentDTO> getAssignmentsForGame(UUID gameId) {
        return crewAssignmentRepository.findByGameId(gameId).stream()
                .map(this::mapToAssignmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CrewAssignmentDTO> getAssignmentsForUser(UUID userId) {
        return crewAssignmentRepository.findByUserId(userId).stream()
                .map(this::mapToAssignmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CrewAssignmentDTO assignCrewMember(CrewAssignment assignment) {
        // Validate that the game, user and position exist
        Game game = gameRepository.findById(assignment.getGame().getId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Game not found with id: " + assignment.getGame().getId()));

        FrogCrewUser user = userRepository.findById(assignment.getUser().getId())
                .orElseThrow(
                        () -> new EntityNotFoundException("User not found with id: " + assignment.getUser().getId()));

        Position position = positionRepository.findById(assignment.getPosition().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Position not found with id: " + assignment.getPosition().getId()));

        // Check if the user already has an assignment for this game
        Optional<CrewAssignment> existingAssignment = crewAssignmentRepository.findByGameAndUser(game, user);

        CrewAssignment savedAssignment;
        if (existingAssignment.isPresent()) {
            // Update existing assignment
            CrewAssignment existing = existingAssignment.get();
            existing.setPosition(position);
            savedAssignment = crewAssignmentRepository.save(existing);
        } else {
            // Create new assignment
            assignment.setGame(game);
            assignment.setUser(user);
            assignment.setPosition(position);
            savedAssignment = crewAssignmentRepository.save(assignment);
        }

        return mapToAssignmentDTO(savedAssignment);
    }

    @Override
    public List<CrewAssignmentDTO> assignCrewMembersBulk(BulkAssignmentDTO bulkAssignment) {
        List<CrewAssignmentDTO> results = new ArrayList<>();

        // Get the game
        Game game = gameRepository.findById(bulkAssignment.getGameId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Game not found with id: " + bulkAssignment.getGameId()));

        for (BulkAssignmentDTO.CrewMemberAssignmentDTO assignmentDTO : bulkAssignment.getAssignments()) {
            // Create a new assignment
            CrewAssignment assignment = new CrewAssignment();

            // Get the user
            FrogCrewUser user = userRepository.findById(assignmentDTO.getUserId())
                    .orElseThrow(
                            () -> new EntityNotFoundException("User not found with id: " + assignmentDTO.getUserId()));

            // Get the position
            Position position = positionRepository.findById(assignmentDTO.getPositionId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Position not found with id: " + assignmentDTO.getPositionId()));

            // Check for existing assignment
            Optional<CrewAssignment> existingAssignment = crewAssignmentRepository.findByGameAndUser(game, user);

            CrewAssignment savedAssignment;
            if (existingAssignment.isPresent()) {
                // Update existing assignment
                CrewAssignment existing = existingAssignment.get();
                existing.setPosition(position);
                savedAssignment = crewAssignmentRepository.save(existing);
            } else {
                // Set the game, user and position
                assignment.setGame(game);
                assignment.setUser(user);
                assignment.setPosition(position);
                savedAssignment = crewAssignmentRepository.save(assignment);
            }

            results.add(mapToAssignmentDTO(savedAssignment));
        }

        return results;
    }

    @Override
    public void removeAssignment(UUID assignmentId) {
        if (!crewAssignmentRepository.existsById(assignmentId)) {
            throw new EntityNotFoundException("Assignment not found with id: " + assignmentId);
        }

        crewAssignmentRepository.deleteById(assignmentId);
    }

    private boolean hasOverlappingAssignments(FrogCrewUser user, CrewSchedule schedule) {
        // Implement logic to check for overlapping assignments
        return false;
    }

    private List<FrogCrewUser> findQualifiedUsers(Position position) {
        // Update to use the new positions field format
        return userRepository.findAll().stream()
                .filter(user -> user.getPositions().contains(position.getCode()))
                .collect(Collectors.toList());
    }

    private FrogCrewUser selectUserWithLowestLoad(List<FrogCrewUser> users, CrewSchedule schedule) {
        return users.stream()
                .min(Comparator.comparingLong(user -> crewAssignmentRepository.countByUser(user)))
                .orElse(null);
    }

    private CrewAssignmentDTO mapToAssignmentDTO(CrewAssignment assignment) {
        CrewAssignmentDTO dto = new CrewAssignmentDTO();
        dto.setId(assignment.getId());
        dto.setGameId(assignment.getGame().getId());
        dto.setGameName(assignment.getGame().getDisplayName());
        dto.setUserId(assignment.getUser().getId());
        dto.setUserName(assignment.getUser().getFirstName() + " " + assignment.getUser().getLastName());
        dto.setPositionId(assignment.getPosition().getId());
        dto.setPositionName(assignment.getPosition().getDisplayName());
        dto.setUserFirstName(assignment.getUser().getFirstName());
        dto.setUserLastName(assignment.getUser().getLastName());
        dto.setAdminOverride(assignment.isAdminOverride());
        // Set report time if available
        if (assignment.getReportTime() != null) {
            dto.setReportTime(assignment.getReportTime());
        }
        return dto;
    }
}