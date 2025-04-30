package com.frogcrew.frogcrew.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "crew_assignments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_schedule_id")
    private CrewSchedule crewSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private FrogCrewUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @Column(nullable = false)
    private boolean adminOverride;

    @Column
    private BigDecimal payRate;

    @Column(nullable = false)
    private BigDecimal totalPay;

    @Column
    private String performanceNotes;

    @Column
    private Integer performanceRating;

    @Column(name = "report_time")
    private LocalTime reportTime;

    @PrePersist
    protected void onCreate() {
        if (payRate == null) {
            payRate = position.getDefaultPayRate();
        }
        if (totalPay == null) {
            totalPay = payRate;
        }
    }
}