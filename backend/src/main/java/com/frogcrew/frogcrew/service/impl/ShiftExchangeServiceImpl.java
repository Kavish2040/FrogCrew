package com.frogcrew.frogcrew.service.impl;

import com.frogcrew.frogcrew.domain.model.*;
import com.frogcrew.frogcrew.repository.*;
import com.frogcrew.frogcrew.service.ShiftExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShiftExchangeServiceImpl implements ShiftExchangeService {

    private final ShiftExchangeRepository shiftExchangeRepository;
    private final ShiftExchangeOfferRepository offerRepository;
    private final CrewAssignmentRepository crewAssignmentRepository;
    private final NotificationRepository notificationRepository;
    private final FrogCrewUserRepository userRepository;

    @Override
    @Transactional
    public ShiftExchange requestExchange(ShiftExchange exchange) {
        // Validate the exchange request
        validateExchangeRequest(exchange);

        // Create the exchange
        exchange.setStatus(ExchangeState.PENDING);
        exchange.setRequestedAt(ZonedDateTime.now());
        ShiftExchange savedExchange = shiftExchangeRepository.save(exchange);

        // Create offers for qualified users
        createExchangeOffers(savedExchange);

        return savedExchange;
    }

    @Override
    @Transactional
    public ShiftExchange acceptExchange(UUID id, UUID userId) {
        ShiftExchange exchange = shiftExchangeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exchange not found"));

        // Find the user who is accepting the exchange
        FrogCrewUser acceptingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update the exchange status
        exchange.setStatus(ExchangeState.ACCEPTED);
        exchange.setAcceptedBy(acceptingUser);
        exchange.setAcceptedAt(ZonedDateTime.now());

        // Create notifications
        createExchangeNotifications(exchange);

        return shiftExchangeRepository.save(exchange);
    }

    @Override
    @Transactional
    public ShiftExchange rejectExchange(UUID id) {
        ShiftExchange exchange = shiftExchangeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exchange not found"));

        exchange.setStatus(ExchangeState.REJECTED);
        exchange.setRejectedAt(ZonedDateTime.now());

        return shiftExchangeRepository.save(exchange);
    }

    @Override
    @Transactional
    public ShiftExchange approveExchange(UUID id) {
        ShiftExchange exchange = shiftExchangeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exchange not found"));

        if (exchange.getStatus() != ExchangeState.ACCEPTED) {
            throw new RuntimeException("Only accepted exchanges can be approved");
        }

        // Update the crew assignments
        CrewAssignment originalAssignment = crewAssignmentRepository.findByGameAndUser(
                exchange.getGame(), exchange.getRequestedBy())
                .orElseThrow(() -> new RuntimeException("Original assignment not found"));

        originalAssignment.setUser(exchange.getAcceptedBy());

        // Update exchange status
        exchange.setStatus(ExchangeState.APPROVED);
        exchange.setApprovedAt(ZonedDateTime.now());

        return shiftExchangeRepository.save(exchange);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftExchange> getUserExchanges(UUID userId) {
        return shiftExchangeRepository.findByRequestedByIdOrAcceptedById(userId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftExchange> getAllExchanges() {
        return shiftExchangeRepository.findAll();
    }

    private void validateExchangeRequest(ShiftExchange exchange) {
        // Check if the game is more than 24 hours away
        if (exchange.getGame().getEventDateTime().isBefore(ZonedDateTime.now().plusHours(24))) {
            throw new RuntimeException("Cannot request exchange for games less than 24 hours away");
        }

        // Check if the user is actually assigned to the game
        if (!crewAssignmentRepository.existsByGameAndUser(exchange.getGame(), exchange.getRequestedBy())) {
            throw new RuntimeException("User is not assigned to this game");
        }
    }

    private void createExchangeOffers(ShiftExchange exchange) {
        // Find qualified users who are available
        List<FrogCrewUser> qualifiedUsers = findQualifiedUsers(exchange);

        // Create offers for each qualified user
        qualifiedUsers.forEach(user -> {
            ShiftExchangeOffer offer = ShiftExchangeOffer.builder()
                    .exchange(exchange)
                    .toUser(user)
                    .offeredAt(ZonedDateTime.now())
                    .accepted(false)
                    .build();
            offerRepository.save(offer);
        });
    }

    private List<FrogCrewUser> findQualifiedUsers(ShiftExchange exchange) {
        // Implementation to find qualified users who:
        // 1. Have the required position qualification
        // 2. Are available for the game time
        // 3. Don't have overlapping assignments
        return List.of(); // TODO: Implement this
    }

    private void createExchangeNotifications(ShiftExchange exchange) {
        // Create notification for the original user
        Notification originalUserNotification = Notification.builder()
                .user(exchange.getRequestedBy())
                .type("EXCHANGE_ACCEPTED")
                .title("Exchange Accepted")
                .body(String.format("Your exchange request for %s has been accepted by %s",
                        exchange.getGame().getDisplayName(),
                        exchange.getAcceptedBy().getFirstName()))
                .read(false)
                .createdAt(ZonedDateTime.now())
                .build();
        notificationRepository.save(originalUserNotification);

        // Create notification for the accepting user
        Notification acceptingUserNotification = Notification.builder()
                .user(exchange.getAcceptedBy())
                .type("EXCHANGE_ACCEPTED")
                .title("Exchange Accepted")
                .body(String.format("You have accepted the exchange request for %s",
                        exchange.getGame().getDisplayName()))
                .read(false)
                .createdAt(ZonedDateTime.now())
                .build();
        notificationRepository.save(acceptingUserNotification);
    }
}