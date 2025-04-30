package com.frogcrew.frogcrew.service.impl;

import com.frogcrew.frogcrew.domain.model.Availability;
import com.frogcrew.frogcrew.domain.model.Game;
import com.frogcrew.frogcrew.domain.model.FrogCrewUser;
import com.frogcrew.frogcrew.repository.AvailabilityRepository;
import com.frogcrew.frogcrew.repository.GameRepository;
import com.frogcrew.frogcrew.repository.FrogCrewUserRepository;
import com.frogcrew.frogcrew.service.AvailabilityService;
import com.frogcrew.frogcrew.service.dto.BulkAvailabilityDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final FrogCrewUserRepository userRepository;
    private final GameRepository gameRepository;

    @Override
    @Transactional
    public Availability submitAvailability(Availability availability) {
        // Validate and set required fields
        if (availability.getUserId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (availability.getGameId() == null) {
            throw new IllegalArgumentException("Game ID is required");
        }

        // Fetch and set the user
        FrogCrewUser user = userRepository.findById(availability.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + availability.getUserId()));
        availability.setUser(user);

        // Fetch and set the game
        Game game = gameRepository.findById(availability.getGameId())
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + availability.getGameId()));
        availability.setGame(game);

        return availabilityRepository.save(availability);
    }

    @Override
    @Transactional
    public Availability editAvailability(UUID id, Availability availability) {
        Availability existing = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found"));

        existing.setStatus(availability.getStatus());
        existing.setComment(availability.getComment());

        return availabilityRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Availability> getUserAvailability(UUID userId) {
        return availabilityRepository.findByUser_Id(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Availability> getGameAvailability(UUID gameId) {
        return availabilityRepository.findByGame_Id(gameId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Availability> getUserAvailabilityBySchedule(UUID userId, UUID scheduleId) {
        // Get all user availabilities
        List<Availability> userAvailabilities = availabilityRepository.findByUser_Id(userId);

        // Filter by games that belong to the specified schedule
        return userAvailabilities.stream()
                .filter(availability -> availability.getGame() != null
                        && availability.getGame().getSchedule() != null
                        && scheduleId.equals(availability.getGame().getSchedule().getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Availability> getUserAvailabilityBySeason(UUID userId, String season) {
        // Get all user availabilities
        List<Availability> userAvailabilities = availabilityRepository.findByUser_Id(userId);

        // Filter by games that belong to the specified season
        return userAvailabilities.stream()
                .filter(availability -> availability.getGame() != null
                        && season.equals(availability.getGame().getSeason()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Availability> submitBulkAvailability(BulkAvailabilityDTO bulkAvailability) {
        List<Availability> results = new ArrayList<>();

        // Get the user
        FrogCrewUser user = userRepository.findById(bulkAvailability.getUserId())
                .orElseThrow(
                        () -> new EntityNotFoundException("User not found with id: " + bulkAvailability.getUserId()));

        for (BulkAvailabilityDTO.AvailabilityDateDTO availabilityDTO : bulkAvailability.getAvailabilities()) {
            Availability availability = new Availability();
            availability.setUser(user);
            availability.setDate(availabilityDTO.getDate());
            availability.setStatus(availabilityDTO.getStatus());
            availability.setNotes(availabilityDTO.getNotes());

            // If a gameId is provided, associate with the game
            if (availabilityDTO.getGameId() != null) {
                Game game = gameRepository.findById(availabilityDTO.getGameId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Game not found with id: " + availabilityDTO.getGameId()));
                availability.setGame(game);
            }

            results.add(availabilityRepository.save(availability));
        }

        return results;
    }
}