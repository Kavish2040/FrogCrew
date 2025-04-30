package com.frogcrew.frogcrew.repository;

import com.frogcrew.frogcrew.domain.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, UUID> {
    List<Availability> findByUser_Id(UUID userId);

    List<Availability> findByGame_Id(UUID gameId);
}