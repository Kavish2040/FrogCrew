package com.frogcrew.frogcrew.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemDetails {
    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;
    private Instant timestamp;
    private Map<String, Object> properties;

    public static ProblemDetailsBuilder builder() {
        return new ProblemDetailsBuilder()
                .timestamp(Instant.now());
    }
}