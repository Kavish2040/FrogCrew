package com.frogcrew.frogcrew.service;

import java.io.OutputStream;
import java.util.UUID;

public interface FinancialReportService {
    void generateFinancialReport(String season, OutputStream outputStream);

    void generatePositionReport(String positionCode, String season, OutputStream outputStream);

    void generateUserReport(UUID userId, String season, OutputStream outputStream, String format);
}