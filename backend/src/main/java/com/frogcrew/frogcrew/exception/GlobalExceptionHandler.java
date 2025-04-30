package com.frogcrew.frogcrew.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetails> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ProblemDetails.builder()
                        .type("https://frogcrew.com/errors/bad-request")
                        .title("Bad Request")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .detail(ex.getMessage())
                        .instance(request.getDescription(false))
                        .build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetails> handleEntityNotFoundException(
            EntityNotFoundException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ProblemDetails.builder()
                        .type("https://frogcrew.com/errors/not-found")
                        .title("Not Found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .detail(ex.getMessage())
                        .instance(request.getDescription(false))
                        .build());
    }

    @ExceptionHandler(BusinessRuleViolation.class)
    public ResponseEntity<ProblemDetails> handleBusinessRuleViolation(
            BusinessRuleViolation ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ProblemDetails.builder()
                        .type("https://frogcrew.com/errors/business-rule-violation")
                        .title("Business Rule Violation")
                        .status(HttpStatus.CONFLICT.value())
                        .detail(ex.getMessage())
                        .instance(request.getDescription(false))
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetails> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ProblemDetails.builder()
                        .type("https://frogcrew.com/errors/forbidden")
                        .title("Forbidden")
                        .status(HttpStatus.FORBIDDEN.value())
                        .detail(ex.getMessage())
                        .instance(request.getDescription(false))
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleGenericException(
            Exception ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ProblemDetails.builder()
                        .type("https://frogcrew.com/errors/internal-server-error")
                        .title("Internal Server Error")
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .detail("An unexpected error occurred")
                        .instance(request.getDescription(false))
                        .build());
    }
}