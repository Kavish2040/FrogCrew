package com.frogcrew.frogcrew.repository;

import com.frogcrew.frogcrew.domain.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {
    List<Game> findByEventDateTimeBetween(ZonedDateTime start, ZonedDateTime end);

    List<Game> findBySport(String sport);

    List<Game> findBySeason(String season);

    Page<Game> findByEventDateTimeAfter(ZonedDateTime dateTime, Pageable pageable);

    List<Game> findByScheduleId(UUID scheduleId);
}