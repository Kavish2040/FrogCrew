package com.frogcrew.frogcrew.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "availabilities")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private FrogCrewUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "user_id", insertable = false, updatable = false)
    private UUID userId;

    @Column(name = "game_id", insertable = false, updatable = false)
    private UUID gameId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AvailabilityStatus status;

    // Added for compatibility with assignment requirements
    @Column(name = "availability")
    private Boolean availability;

    @Column(length = 500)
    private String comment;

    @Column(nullable = false)
    private ZonedDateTime submittedAt;

    @Column(nullable = false)
    private ZonedDateTime lastModifiedAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "notes")
    private String notes;

    @PrePersist
    protected void onCreate() {
        submittedAt = ZonedDateTime.now();
        lastModifiedAt = ZonedDateTime.now();
        active = true;
        isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifiedAt = ZonedDateTime.now();
    }

    // Added for compatibility with other classes
    public LocalDate getDate() {
        if (game != null && game.getEventDateTime() != null) {
            return game.getEventDateTime().toLocalDate();
        }
        return date;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getGameId() {
        return gameId;
    }

    // Added helper methods for the availability field
    public Boolean getAvailability() {
        if (status == null)
            return null;
        return status == AvailabilityStatus.AVAILABLE;
    }

    public void setAvailability(Boolean available) {
        if (available == null)
            return;
        this.status = available ? AvailabilityStatus.AVAILABLE : AvailabilityStatus.UNAVAILABLE;
        this.availability = available;
    }

    // Explicit getter and setter for isActive
    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}