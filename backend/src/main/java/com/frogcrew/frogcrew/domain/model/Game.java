package com.frogcrew.frogcrew.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String sport;

    @Column(nullable = false)
    private String opponent;

    @Column(nullable = false)
    private String season;

    @Column(nullable = false)
    private ZonedDateTime eventDateTime;

    @Column(nullable = false)
    private String venue;

    @Column(nullable = false)
    private String network;
    @ManyToOne
    @JoinColumn(name = "game_schedule_id") // FK in Game table
    private GameSchedule schedule;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CrewAssignment> assignments;

    public String getDisplayName() {
        return String.format("%s vs %s", sport, opponent);
    }
}