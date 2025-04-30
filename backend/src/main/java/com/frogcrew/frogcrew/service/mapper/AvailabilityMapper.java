package com.frogcrew.frogcrew.service.mapper;

import com.frogcrew.frogcrew.domain.model.Availability;
import com.frogcrew.frogcrew.service.dto.AvailabilityDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Availability} and its DTO
 * {@link AvailabilityDTO}.
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AvailabilityMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "game.id", target = "gameId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "availability", target = "availability")
    @Mapping(source = "comment", target = "comment")
    @Mapping(source = "submittedAt", target = "submittedAt")
    @Mapping(source = "lastModifiedAt", target = "lastModifiedAt")
    @Mapping(source = "active", target = "active")
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "notes", target = "notes")
    AvailabilityDTO toDto(Availability availability);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "game", ignore = true)
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "gameId", source = "gameId")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "isActive", source = "isActive")
    Availability toEntity(AvailabilityDTO availabilityDTO);
}