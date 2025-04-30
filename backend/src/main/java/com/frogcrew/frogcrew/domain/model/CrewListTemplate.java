package com.frogcrew.frogcrew.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "crew_list_templates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewListTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String sport;

    @ElementCollection
    @CollectionTable(name = "template_positions", joinColumns = @JoinColumn(name = "template_id"))
    @Column(name = "position_code")
    private Set<String> positions;
}