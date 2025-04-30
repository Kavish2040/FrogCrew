package com.frogcrew.frogcrew.service;

import com.frogcrew.frogcrew.domain.model.CrewListTemplate;

import java.util.List;
import java.util.UUID;

public interface CrewListTemplateService {
    CrewListTemplate createTemplate(CrewListTemplate template);

    List<CrewListTemplate> getAllTemplates();

    CrewListTemplate getTemplate(UUID id);

    CrewListTemplate findTemplateById(UUID id);

    CrewListTemplate getTemplateById(UUID id);

    CrewListTemplate updateTemplate(UUID id, CrewListTemplate template);

    void deleteTemplate(UUID id);
}