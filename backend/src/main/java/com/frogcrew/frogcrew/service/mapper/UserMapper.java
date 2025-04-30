package com.frogcrew.frogcrew.service.mapper;

import com.frogcrew.frogcrew.domain.model.FrogCrewUser;
import com.frogcrew.frogcrew.domain.model.Position;
import com.frogcrew.frogcrew.repository.PositionRepository;
import com.frogcrew.frogcrew.service.dto.UserDTO;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

    @Autowired
    private PositionRepository positionRepository;

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "positionIds", expression = "java(mapPositionsToIds(user.getPositions()))")
    public abstract UserDTO toDto(FrogCrewUser user);

    @Mapping(target = "positions", expression = "java(mapIdsToPositions(userDTO.getPositionIds()))")
    public abstract FrogCrewUser toEntity(UserDTO userDTO);

    protected List<UUID> mapPositionsToIds(List<Position> positions) {
        if (positions == null) {
            return new ArrayList<>();
        }
        return positions.stream()
                .map(Position::getId)
                .collect(Collectors.toList());
    }

    protected List<Position> mapIdsToPositions(List<UUID> positionIds) {
        if (positionIds == null) {
            return new ArrayList<>();
        }
        return positionIds.stream()
                .map(id -> positionRepository.findById(id).orElse(null))
                .filter(position -> position != null)
                .collect(Collectors.toList());
    }
}