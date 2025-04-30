package com.frogcrew.frogcrew.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.HashSet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "game_schedules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    @Column(nullable = false)
    private String season;

    @Column(nullable = false)
    private String sport;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleStatus status;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Game> games = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private CrewListTemplate template;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }

    // Method to convert ZonedDateTime to LocalDateTime for DTO
    public LocalDateTime getCreatedAtAsLocalDateTime() {
        return createdAt != null ? createdAt.toLocalDateTime() : null;
    }

    public LocalDateTime getUpdatedAtAsLocalDateTime() {
        return updatedAt != null ? updatedAt.toLocalDateTime() : null;
    }
}