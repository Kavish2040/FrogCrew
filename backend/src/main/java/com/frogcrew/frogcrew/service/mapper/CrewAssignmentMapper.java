package com.frogcrew.frogcrew.service.mapper;

import com.frogcrew.frogcrew.domain.model.CrewAssignment;
import com.frogcrew.frogcrew.service.dto.CrewAssignmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link CrewAssignment} and its DTO
 * {@link CrewAssignmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CrewAssignmentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.firstName", target = "userFirstName")
    @Mapping(source = "user.lastName", target = "userLastName")
    @Mapping(source = "position.id", target = "positionId")
    @Mapping(source = "position.displayName", target = "positionName")
    @Mapping(source = "game.id", target = "gameId")
    @Mapping(source = "adminOverride", target = "adminOverride")
    CrewAssignmentDTO toDto(CrewAssignment crewAssignment);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "game", ignore = true)
    @Mapping(target = "position", ignore = true)
    CrewAssignment toEntity(CrewAssignmentDTO crewAssignmentDTO);
}