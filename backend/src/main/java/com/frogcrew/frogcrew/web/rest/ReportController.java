package com.frogcrew.frogcrew.web.rest;

import com.frogcrew.frogcrew.service.FinancialReportService;
import com.frogcrew.frogcrew.service.ReportGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportGenerationService reportGenerationService;
    private final FinancialReportService financialReportService;

    @GetMapping("/report/financial/{season}/{sport}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> getFinancialReport(
            @PathVariable String season,
            @PathVariable String sport) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        financialReportService.generateFinancialReport(season, outputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "financial-report.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }

    @GetMapping("/report/position/{positionId}/{season}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> getPositionReport(
            @PathVariable UUID positionId,
            @PathVariable String season) {
        try {
            byte[] report = reportGenerationService.generatePositionReport(positionId, season);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=position-report.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/report/position/{positionId}/{season}/{sport}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> getPositionReportBySport(
            @PathVariable UUID positionId,
            @PathVariable String season,
            @PathVariable String sport) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        financialReportService.generatePositionReport(positionId.toString(), season, outputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "position-report.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }

    @GetMapping("/report/crewMember/{userId}/{season}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> getCrewMemberReport(
            @PathVariable UUID userId,
            @PathVariable String season) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        financialReportService.generateUserReport(userId, season, outputStream, "CSV");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "crew-member-report.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }
}