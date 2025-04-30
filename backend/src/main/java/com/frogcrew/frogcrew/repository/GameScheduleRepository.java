package com.frogcrew.frogcrew.repository;

import com.frogcrew.frogcrew.domain.model.GameSchedule;
import com.frogcrew.frogcrew.domain.model.CrewListTemplate;
import com.frogcrew.frogcrew.domain.model.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameScheduleRepository extends JpaRepository<GameSchedule, UUID> {
    List<GameSchedule> findByTemplateAndStatus(CrewListTemplate template, ScheduleStatus status);
}