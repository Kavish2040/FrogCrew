package com.frogcrew.frogcrew.service.impl;

import com.frogcrew.frogcrew.domain.model.CrewListTemplate;
import com.frogcrew.frogcrew.domain.model.GameSchedule;
import com.frogcrew.frogcrew.domain.model.ScheduleStatus;
import com.frogcrew.frogcrew.repository.CrewListTemplateRepository;
import com.frogcrew.frogcrew.repository.GameScheduleRepository;
import com.frogcrew.frogcrew.service.CrewListTemplateService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CrewListTemplateServiceImpl implements CrewListTemplateService {

    private final CrewListTemplateRepository templateRepository;
    private final GameScheduleRepository gameScheduleRepository;

    @Override
    @Transactional
    public CrewListTemplate createTemplate(CrewListTemplate template) {
        if (templateRepository.existsByName(template.getName())) {
            throw new RuntimeException("Template with this name already exists");
        }
        return templateRepository.save(template);
    }

    @Override
    @Transactional(readOnly = true)
    public CrewListTemplate getTemplate(UUID id) {
        return findTemplateById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CrewListTemplate> getAllTemplates() {
        return templateRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public CrewListTemplate findTemplateById(UUID id) {

        return templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
    }

    @Override
    @Transactional
    public CrewListTemplate updateTemplate(UUID id, CrewListTemplate template) {
        CrewListTemplate existing = findTemplateById(id);
        if (!existing.getName().equals(template.getName()) &&
                templateRepository.existsByName(template.getName())) {
            throw new RuntimeException("Template with this name already exists");
        }

        existing.setName(template.getName());
        existing.setSport(template.getSport());
        existing.setPositions(template.getPositions());

        return templateRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteTemplate(UUID id) {
        CrewListTemplate template = findTemplateById(id);

        // Check if template is referenced by any published schedule
        List<GameSchedule> schedules = gameScheduleRepository.findByTemplateAndStatus(template,
                ScheduleStatus.PUBLISHED);
        if (!schedules.isEmpty()) {
            throw new RuntimeException("Cannot delete template referenced by published schedules");
        }

        templateRepository.delete(template);
    }

    @Override
    public CrewListTemplate getTemplateById(UUID id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Template not found with id: " + id));
    }
}