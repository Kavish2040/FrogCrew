package com.frogcrew.frogcrew.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "shift_exchange_offers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftExchangeOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_id", nullable = false)
    private ShiftExchange exchange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id", nullable = false)
    private FrogCrewUser toUser;

    @Column(nullable = false)
    private ZonedDateTime offeredAt;

    @Column(nullable = false)
    private boolean accepted;
}