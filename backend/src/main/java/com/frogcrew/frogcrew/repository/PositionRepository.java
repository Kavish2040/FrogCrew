package com.frogcrew.frogcrew.repository;

import com.frogcrew.frogcrew.domain.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PositionRepository extends JpaRepository<Position, UUID> {
    Optional<Position> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT CASE WHEN COUNT(ca) > 0 THEN true ELSE false END " +
            "FROM CrewAssignment ca WHERE ca.position.id = :positionId")
    boolean isPositionInUse(UUID positionId);
}