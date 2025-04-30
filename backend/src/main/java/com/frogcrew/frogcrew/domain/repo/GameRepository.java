// package com.frogcrew.frogcrew.domain.repo;

// import com.frogcrew.frogcrew.domain.model.Game;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import java.time.ZonedDateTime;
// import java.util.List;
// import java.util.UUID;

// @Repository
// public interface GameRepository extends JpaRepository<Game, UUID> {
// List<Game> findByEventDateTimeBetween(ZonedDateTime start, ZonedDateTime
// end);

// List<Game> findBySport(String sport);

// Page<Game> findByEventDateTimeAfter(ZonedDateTime dateTime, Pageable
// pageable);
// }