package com.frogcrew.frogcrew.repository;

import com.frogcrew.frogcrew.domain.model.ShiftExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShiftExchangeRepository extends JpaRepository<ShiftExchange, UUID> {
    List<ShiftExchange> findByRequestedByIdOrAcceptedById(UUID requestedById, UUID acceptedById);
}