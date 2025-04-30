package com.frogcrew.frogcrew.service;

import com.frogcrew.frogcrew.domain.model.Position;
import com.frogcrew.frogcrew.domain.model.PositionProperty;

import java.util.List;
import java.util.UUID;

public interface PositionService {
    Position createPosition(Position position);

    Position updatePosition(UUID id, Position position);

    void deletePosition(UUID id);

    Position getPosition(UUID id);

    Position getPositionById(UUID id);

    List<Position> getAllPositions();

    PositionProperty createPositionProperty(PositionProperty property);

    PositionProperty updatePositionProperty(UUID positionId, String gameType, PositionProperty property);

    List<PositionProperty> getPositionPropertiesByGameType(String gameType);
}