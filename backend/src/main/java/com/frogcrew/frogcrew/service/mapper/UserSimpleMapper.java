package com.frogcrew.frogcrew.service.mapper;

import com.frogcrew.frogcrew.domain.model.FrogCrewUser;
import com.frogcrew.frogcrew.service.dto.UserSimpleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserSimpleMapper {
    @Mapping(target = "userId", source = "id")
    @Mapping(target = "fullName", expression = "java(user.getFullName())")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    UserSimpleDTO toDto(FrogCrewUser user);
}