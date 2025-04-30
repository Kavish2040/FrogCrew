package com.frogcrew.frogcrew.repository;

import com.frogcrew.frogcrew.domain.model.Position;
import com.frogcrew.frogcrew.domain.model.PositionProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PositionPropertyRepository extends JpaRepository<PositionProperty, UUID> {
    List<PositionProperty> findByGameType(String gameType);

    Optional<PositionProperty> findByPositionAndGameType(Position position, String gameType);
}