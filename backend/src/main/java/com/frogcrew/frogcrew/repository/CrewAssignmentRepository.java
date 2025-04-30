package com.frogcrew.frogcrew.repository;

import com.frogcrew.frogcrew.domain.model.CrewAssignment;
import com.frogcrew.frogcrew.domain.model.CrewSchedule;
import com.frogcrew.frogcrew.domain.model.Game;
import com.frogcrew.frogcrew.domain.model.FrogCrewUser;
import com.frogcrew.frogcrew.domain.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CrewAssignmentRepository extends JpaRepository<CrewAssignment, UUID> {
    List<CrewAssignment> findByGame(Game game);

    List<CrewAssignment> findByUser(FrogCrewUser user);

    Optional<CrewAssignment> findByGameAndUser(Game game, FrogCrewUser user);

    boolean existsByGameAndUser(Game game, FrogCrewUser user);

    boolean existsByGameAndUserAndPosition(Game game, FrogCrewUser user, Position position);

    Optional<CrewAssignment> findByGameAndPosition(Game game, Position position);

    long countByUserAndGame_EventDateTimeBetween(FrogCrewUser user, ZonedDateTime start, ZonedDateTime end);

    boolean existsByUserAndGame_EventDateTimeBetween(FrogCrewUser user, ZonedDateTime start, ZonedDateTime end);

    List<CrewAssignment> findByPosition(Position position);

    // Crew Schedule related methods
    List<CrewAssignment> findByCrewSchedule(CrewSchedule crewSchedule);

    Optional<CrewAssignment> findByCrewScheduleAndPosition(CrewSchedule crewSchedule, Position position);

    Optional<CrewAssignment> findByCrewScheduleAndUser(CrewSchedule crewSchedule, FrogCrewUser user);

    boolean existsByCrewScheduleAndUser(CrewSchedule crewSchedule, FrogCrewUser user);

    long countByUser(FrogCrewUser user);

    List<CrewAssignment> findByGameId(UUID gameId);

    List<CrewAssignment> findByUserId(UUID userId);
}