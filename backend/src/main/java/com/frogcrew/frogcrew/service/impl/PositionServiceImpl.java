package com.frogcrew.frogcrew.service.impl;

import com.frogcrew.frogcrew.domain.model.Position;
import com.frogcrew.frogcrew.domain.model.PositionProperty;
import com.frogcrew.frogcrew.repository.PositionPropertyRepository;
import com.frogcrew.frogcrew.repository.PositionRepository;
import com.frogcrew.frogcrew.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final PositionPropertyRepository positionPropertyRepository;

    @Override
    @Transactional
    public Position createPosition(Position position) {
        if (positionRepository.existsByCode(position.getCode())) {
            throw new RuntimeException("Position with this code already exists");
        }
        return positionRepository.save(position);
    }

    @Override
    @Transactional
    public Position updatePosition(UUID id, Position position) {
        Position existing = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        // Check if the new code conflicts with other positions
        if (!existing.getCode().equals(position.getCode()) &&
                positionRepository.existsByCode(position.getCode())) {
            throw new RuntimeException("Position with this code already exists");
        }

        existing.setCode(position.getCode());
        existing.setDisplayName(position.getDisplayName());
        existing.setDescription(position.getDescription());
        existing.setRequiredQualifications(position.getRequiredQualifications());

        return positionRepository.save(existing);
    }

    @Override
    @Transactional
    public void deletePosition(UUID id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        // Check if position is in use
        if (positionRepository.isPositionInUse(id)) {
            throw new RuntimeException("Cannot delete position that is in use");
        }

        positionRepository.delete(position);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Position getPosition(UUID id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Position getPositionById(UUID id) {
        return positionRepository.findById(id)
                .orElse(null);
    }

    @Override
    @Transactional
    public PositionProperty createPositionProperty(PositionProperty property) {
        // Verify the position exists
        Position position = positionRepository.findById(property.getPosition().getId())
                .orElseThrow(() -> new RuntimeException("Position not found"));

        // Set the position reference
        property.setPosition(position);

        return positionPropertyRepository.save(property);
    }

    @Override
    @Transactional
    public PositionProperty updatePositionProperty(UUID positionId, String gameType, PositionProperty property) {
        // Verify the position exists
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        // Find the existing property
        PositionProperty existing = positionPropertyRepository.findByPositionAndGameType(position, gameType)
                .orElseThrow(() -> new RuntimeException("Position property not found"));

        // Update properties
        existing.setProperties(property.getProperties());
        existing.setDescription(property.getDescription());

        return positionPropertyRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionProperty> getPositionPropertiesByGameType(String gameType) {
        return positionPropertyRepository.findByGameType(gameType);
    }
}