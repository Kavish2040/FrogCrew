package com.frogcrew.frogcrew.web.rest;

import com.frogcrew.frogcrew.domain.model.CrewListTemplate;
import com.frogcrew.frogcrew.model.ApiResponse;
import com.frogcrew.frogcrew.service.CrewListTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/crew-templates")
@RequiredArgsConstructor
public class TemplateController {

    private final CrewListTemplateService templateService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CrewListTemplate>>> getAllTemplates() {
        return ResponseEntity.ok(ApiResponse.success("Find Success", templateService.getAllTemplates()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CrewListTemplate>> createTemplate(@RequestBody CrewListTemplate template) {
        return ResponseEntity.ok(ApiResponse.success("Add Success", templateService.createTemplate(template)));
    }

    @GetMapping("/{templateId}")
    public ResponseEntity<ApiResponse<CrewListTemplate>> getTemplateById(@PathVariable UUID templateId) {
        return ResponseEntity.ok(ApiResponse.success("Find Success", templateService.getTemplateById(templateId)));
    }

    @PutMapping("/{templateId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CrewListTemplate>> updateTemplate(
            @PathVariable UUID templateId,
            @RequestBody CrewListTemplate template) {
        template.setId(templateId);
        return ResponseEntity
                .ok(ApiResponse.success("Update Success", templateService.updateTemplate(templateId, template)));
    }

    @DeleteMapping("/{templateId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTemplate(@PathVariable UUID templateId) {
        templateService.deleteTemplate(templateId);
        return ResponseEntity.ok(ApiResponse.success("Delete Success", null));
    }
}