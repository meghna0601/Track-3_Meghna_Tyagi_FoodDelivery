package com.order.exception;

import com.order.config.CorrelationIdFilter;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    public record FieldIssue(String field, String issue) {}
    public record ApiError(String errorCode, String message, List<FieldIssue> details, String correlationId, Instant timestamp) {}

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> notFound(NotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err("NOT_FOUND", ex.getMessage(), List.of(), req));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> conflict(ConflictException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err("CONFLICT", ex.getMessage(), List.of(), req));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> badRequest(BadRequestException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("BAD_REQUEST", ex.getMessage(), List.of(), req));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        var details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new FieldIssue(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("VALIDATION_ERROR", "Validation failed", details, req));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> violation(ConstraintViolationException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("VALIDATION_ERROR", ex.getMessage(), List.of(), req));
    }

    @ExceptionHandler({OptimisticLockException.class})
    public ResponseEntity<ApiError> optimistic(OptimisticLockException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err("OPTIMISTIC_LOCK", "Concurrent update detected", List.of(), req));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> generic(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err("INTERNAL_ERROR", ex.getMessage(), List.of(), req));
    }

    private ApiError err(String code, String msg, List<FieldIssue> details, HttpServletRequest req) {
        return new ApiError(code, msg, details, req.getHeader(CorrelationIdFilter.HEADER), Instant.now());
    }
}
