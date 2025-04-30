package com.frogcrew.frogcrew.service;

import com.frogcrew.frogcrew.domain.model.ShiftExchange;
import java.util.List;
import java.util.UUID;

public interface ShiftExchangeService {
    ShiftExchange requestExchange(ShiftExchange exchange);

    ShiftExchange acceptExchange(UUID id, UUID userId);

    ShiftExchange rejectExchange(UUID id);

    ShiftExchange approveExchange(UUID id);

    List<ShiftExchange> getUserExchanges(UUID userId);

    List<ShiftExchange> getAllExchanges();
}