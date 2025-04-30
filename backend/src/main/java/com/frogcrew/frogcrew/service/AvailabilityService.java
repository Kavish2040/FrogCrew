package com.frogcrew.frogcrew.service;

import com.frogcrew.frogcrew.domain.model.Availability;
import com.frogcrew.frogcrew.service.dto.BulkAvailabilityDTO;
import java.util.List;
import java.util.UUID;

public interface AvailabilityService {
    Availability submitAvailability(Availability availability);

    List<Availability> submitBulkAvailability(BulkAvailabilityDTO bulkAvailability);

    Availability editAvailability(UUID id, Availability availability);

    List<Availability> getUserAvailability(UUID userId);

    List<Availability> getGameAvailability(UUID gameId);

    List<Availability> getUserAvailabilityBySchedule(UUID userId, UUID scheduleId);

    List<Availability> getUserAvailabilityBySeason(UUID userId, String season);
}