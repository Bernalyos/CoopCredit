package com.codeup.coopcredit.infrastructure.adapter.input.rest.advice;

import com.codeup.coopcredit.domain.exception.BusinessRuleException;
import com.codeup.coopcredit.domain.exception.DuplicateResourceException;
import com.codeup.coopcredit.domain.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Request validation failed");
        problemDetail.setTitle("Validation Error");
        problemDetail.setType(URI.create("https://coopcredit.com/errors/validation"));
        problemDetail.setProperty("traceId", MDC.get("traceId"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage());
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setType(URI.create("https://coopcredit.com/errors/not-found"));
        problemDetail.setProperty("traceId", MDC.get("traceId"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        
        return problemDetail;
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ProblemDetail handleDuplicateResourceException(DuplicateResourceException ex) {
        log.warn("Duplicate resource: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage());
        problemDetail.setTitle("Duplicate Resource");
        problemDetail.setType(URI.create("https://coopcredit.com/errors/conflict"));
        problemDetail.setProperty("traceId", MDC.get("traceId"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        
        return problemDetail;
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRuleException(BusinessRuleException ex) {
        log.warn("Business rule violation: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                ex.getMessage());
        problemDetail.setTitle("Business Rule Violation");
        problemDetail.setType(URI.create("https://coopcredit.com/errors/business-rule"));
        problemDetail.setProperty("traceId", MDC.get("traceId"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Invalid argument: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage());
        problemDetail.setTitle("Invalid Argument");
        problemDetail.setType(URI.create("https://coopcredit.com/errors/bad-request"));
        problemDetail.setProperty("traceId", MDC.get("traceId"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        
        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "You don't have permission to access this resource");
        problemDetail.setTitle("Access Denied");
        problemDetail.setType(URI.create("https://coopcredit.com/errors/forbidden"));
        problemDetail.setProperty("traceId", MDC.get("traceId"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        
        return problemDetail;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException(BadCredentialsException ex) {
        log.warn("Authentication failed: Invalid credentials");
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "Invalid username or password");
        problemDetail.setTitle("Authentication Failed");
        problemDetail.setType(URI.create("https://coopcredit.com/errors/unauthorized"));
        problemDetail.setProperty("traceId", MDC.get("traceId"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        
        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("Data integrity violation", ex);
        
        String message = "Data integrity constraint violated";
        if (ex.getMessage() != null && ex.getMessage().contains("unique")) {
            message = "A record with this information already exists";
        }
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                message);
        problemDetail.setTitle("Data Integrity Violation");
        problemDetail.setType(URI.create("https://coopcredit.com/errors/data-integrity"));
        problemDetail.setProperty("traceId", MDC.get("traceId"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.");
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://coopcredit.com/errors/internal"));
        problemDetail.setProperty("traceId", MDC.get("traceId"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        
        return problemDetail;
    }
}
