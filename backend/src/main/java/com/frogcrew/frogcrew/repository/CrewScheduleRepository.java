package com.frogcrew.frogcrew.repository;

import com.frogcrew.frogcrew.domain.model.CrewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CrewScheduleRepository extends JpaRepository<CrewSchedule, UUID> {
    Optional<CrewSchedule> findByGameScheduleId(UUID gameScheduleId);
}