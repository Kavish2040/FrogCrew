package com.frogcrew.frogcrew.repository;

import com.frogcrew.frogcrew.domain.model.ShiftExchange;
import com.frogcrew.frogcrew.domain.model.ShiftExchangeOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShiftExchangeOfferRepository extends JpaRepository<ShiftExchangeOffer, UUID> {
    Optional<ShiftExchangeOffer> findByExchangeAndAcceptedTrue(ShiftExchange exchange);
}