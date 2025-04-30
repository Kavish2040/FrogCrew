package com.frogcrew.frogcrew.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "position_properties")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @Column(nullable = false)
    private String gameType;

    @Column(nullable = false, columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> properties;

    @Column
    private String description;

    // Helper method to get the position name for the API response
    public String getPositionName() {
        return position != null ? position.getDisplayName() : null;
    }

    // Helper method to get the position ID for the API response
    public UUID getPositionId() {
        return position != null ? position.getId() : null;
    }
}