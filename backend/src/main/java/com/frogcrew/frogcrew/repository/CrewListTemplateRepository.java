package com.frogcrew.frogcrew.repository;

import com.frogcrew.frogcrew.domain.model.CrewListTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CrewListTemplateRepository extends JpaRepository<CrewListTemplate, UUID> {
    boolean existsByName(String name);
}