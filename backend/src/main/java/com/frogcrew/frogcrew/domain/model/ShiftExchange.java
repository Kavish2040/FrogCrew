package com.frogcrew.frogcrew.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "shift_exchanges")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftExchange {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by_id", nullable = false)
    private FrogCrewUser requestedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accepted_by_id")
    private FrogCrewUser acceptedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExchangeState status;

    @Column(length = 500)
    private String reason;

    @Column(nullable = false)
    private ZonedDateTime requestedAt;

    @Column
    private ZonedDateTime acceptedAt;

    @Column
    private ZonedDateTime rejectedAt;

    @Column
    private ZonedDateTime approvedAt;

    @Column
    private ZonedDateTime cancelledAt;

    @PrePersist
    protected void onCreate() {
        requestedAt = ZonedDateTime.now();
        status = ExchangeState.PENDING;
    }

    // Additional getters and setters for status transitions
    public void setStatus(ExchangeState newStatus) {
        this.status = newStatus;
        switch (newStatus) {
            case ACCEPTED -> this.acceptedAt = ZonedDateTime.now();
            case REJECTED -> this.rejectedAt = ZonedDateTime.now();
            case APPROVED -> this.approvedAt = ZonedDateTime.now();
            case CANCELLED -> this.cancelledAt = ZonedDateTime.now();
        }
    }

    public ExchangeState getStatus() {
        return this.status;
    }

    public void setAcceptedBy(FrogCrewUser user) {
        this.acceptedBy = user;
        this.acceptedAt = ZonedDateTime.now();
    }

    public FrogCrewUser getAcceptedBy() {
        return this.acceptedBy;
    }

    public FrogCrewUser getRequestedBy() {
        return this.requestedBy;
    }

    public Game getGame() {
        return this.game;
    }
}