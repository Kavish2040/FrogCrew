package com.frogcrew.frogcrew.service.mapper;

import com.frogcrew.frogcrew.domain.model.Game;
import com.frogcrew.frogcrew.service.dto.GameDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = { DateTimeFormatter.class })
public interface GameMapper {
    @Mapping(source = "id", target = "gameId")
    @Mapping(source = "schedule.id", target = "scheduleId")
    @Mapping(expression = "java(game.getEventDateTime().toLocalDate().toString())", target = "gameDate")
    @Mapping(source = "venue", target = "venue")
    @Mapping(source = "opponent", target = "opponent")
    @Mapping(expression = "java(game.getSchedule() != null && game.getSchedule().getStatus() != null && game.getSchedule().getStatus().toString().equals(\"PUBLISHED\"))", target = "isFinalized")
    @Mapping(expression = "java(game.getEventDateTime().toLocalTime().toString())", target = "gameStart")
    GameDTO toDto(Game game);

    @Mapping(target = "id", source = "gameId")
    Game toEntity(GameDTO gameDTO);
}