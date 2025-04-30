package com.frogcrew.frogcrew.web.rest;

import com.frogcrew.frogcrew.domain.model.CrewListTemplate;
import com.frogcrew.frogcrew.service.CrewListTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/template")
@RequiredArgsConstructor
public class CrewListTemplateController {

    private final CrewListTemplateService templateService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CrewListTemplate> createTemplate(@RequestBody CrewListTemplate template) {
        return ResponseEntity.ok(templateService.createTemplate(template));
    }

    @PutMapping("/{templateId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CrewListTemplate> updateTemplate(
            @PathVariable UUID templateId,
            @RequestBody CrewListTemplate template) {
        return ResponseEntity.ok(templateService.updateTemplate(templateId, template));
    }

    @DeleteMapping("/{templateId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTemplate(@PathVariable UUID templateId) {
        templateService.deleteTemplate(templateId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CrewListTemplate>> getAllTemplates() {
        return ResponseEntity.ok(templateService.getAllTemplates());
    }

    @GetMapping("/{templateId}")
    public ResponseEntity<CrewListTemplate> getTemplate(@PathVariable UUID templateId) {
        return ResponseEntity.ok(templateService.getTemplate(templateId));
    }
}