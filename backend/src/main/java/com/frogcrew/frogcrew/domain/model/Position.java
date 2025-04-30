package com.frogcrew.frogcrew.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "positions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private BigDecimal defaultPayRate;

    @Column
    private String description;

    @ElementCollection
    @CollectionTable(name = "position_qualifications", joinColumns = @JoinColumn(name = "position_id"))
    @Column(name = "qualification")
    private Set<String> requiredQualifications;

    @Column(nullable = false)
    private boolean isActive;

    @PrePersist
    protected void onCreate() {
        isActive = true;
    }
}