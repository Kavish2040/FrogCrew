package com.frogcrew.frogcrew.service.impl;

import com.frogcrew.frogcrew.domain.model.FinancialTransaction;
import com.frogcrew.frogcrew.repository.FinancialTransactionRepository;
import com.frogcrew.frogcrew.service.FinancialReportService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinancialReportServiceImpl implements FinancialReportService {

    private final FinancialTransactionRepository transactionRepository;

    @Override
    public void generateFinancialReport(String season, OutputStream outputStream) {
        List<Object[]> gameTotals = transactionRepository.getTotalsByGameForSeason(season);
        BigDecimal seasonTotal = transactionRepository.getTotalForSeason(season);

        StringBuilder csv = new StringBuilder();
        csv.append("Game,Total\n");
        gameTotals.forEach(row -> {
            csv.append(row[0]).append(",").append(row[1]).append("\n");
        });
        csv.append("Season Total,").append(seasonTotal).append("\n");

        try {
            outputStream.write(csv.toString().getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate financial report", e);
        }
    }

    @Override
    public void generatePositionReport(String positionCode, String season, OutputStream outputStream) {
        List<Object[]> positionStats = transactionRepository.getPositionStats(positionCode, season);

        StringBuilder csv = new StringBuilder();
        csv.append("User,Games Worked,Total Hours\n");
        positionStats.forEach(row -> {
            csv.append(row[0]).append(",")
                    .append(row[1]).append(",")
                    .append(row[2]).append("\n");
        });

        try {
            outputStream.write(csv.toString().getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate position report", e);
        }
    }

    @Override
    public void generateUserReport(UUID userId, String season, OutputStream outputStream, String format) {
        List<Object[]> userDetails = transactionRepository.getUserFinancialDetails(userId, season);

        if ("PDF".equalsIgnoreCase(format)) {
            generatePdfReport(userDetails, outputStream);
        } else {
            generateCsvReport(userDetails, outputStream);
        }
    }

    private void generateCsvReport(List<Object[]> userDetails, OutputStream outputStream) {
        StringBuilder csv = new StringBuilder();
        csv.append("Game,Position,Hours,Rate,Total\n");
        userDetails.forEach(row -> {
            csv.append(row[0]).append(",")
                    .append(row[1]).append(",")
                    .append(row[2]).append(",")
                    .append(row[3]).append(",")
                    .append(row[4]).append("\n");
        });

        try {
            outputStream.write(csv.toString().getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate CSV report", e);
        }
    }

    private void generatePdfReport(List<Object[]> userDetails, OutputStream outputStream) {
        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Crew Member Financial Report"));

            Table table = new Table(5);
            table.addCell(new Cell().add(new Paragraph("Game")));
            table.addCell(new Cell().add(new Paragraph("Position")));
            table.addCell(new Cell().add(new Paragraph("Hours")));
            table.addCell(new Cell().add(new Paragraph("Rate")));
            table.addCell(new Cell().add(new Paragraph("Total")));

            userDetails.forEach(row -> {
                table.addCell(new Cell().add(new Paragraph(row[0].toString())));
                table.addCell(new Cell().add(new Paragraph(row[1].toString())));
                table.addCell(new Cell().add(new Paragraph(row[2].toString())));
                table.addCell(new Cell().add(new Paragraph(row[3].toString())));
                table.addCell(new Cell().add(new Paragraph(row[4].toString())));
            });

            document.add(table);
            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }
}