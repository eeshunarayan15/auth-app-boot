package com.eeshu.auth.exception;

import com.eeshu.auth.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ==================== Custom Application Exceptions ====================

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        log.error("User already exists: {}", ex.getMessage());
        ApiResponse<Object> error = new ApiResponse<>("Error", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("User not found: {}", ex.getMessage());
        ApiResponse<Object> error = new ApiResponse<>("Error", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        log.error("Invalid credentials: {}", ex.getMessage());
        ApiResponse<Object> error = new ApiResponse<>("Error", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // ==================== Spring Security Authentication Exceptions ====================

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("Bad credentials: {}", ex.getMessage());
        ApiResponse<Object> error = new ApiResponse<>(
                "Error",
                "Invalid email or password",
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<Object>> handleDisabledException(DisabledException ex) {
        log.error("Account disabled: {}", ex.getMessage());
        ApiResponse<Object> error = new ApiResponse<>(
                "Error",
                "Your account has been disabled. Please contact support.",
                null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiResponse<Object>> handleLockedException(LockedException ex) {
        log.error("Account locked: {}", ex.getMessage());
        ApiResponse<Object> error = new ApiResponse<>(
                "Error",
                "Your account has been locked. Please contact support.",
                null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccountExpiredException(AccountExpiredException ex) {
        log.error("Account expired: {}", ex.getMessage());
        ApiResponse<Object> error = new ApiResponse<>(
                "Error",
                "Your account has expired. Please renew your subscription.",
                null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<ApiResponse<Object>> handleCredentialsExpiredException(CredentialsExpiredException ex) {
        log.error("Credentials expired: {}", ex.getMessage());
        ApiResponse<Object> error = new ApiResponse<>(
                "Error",
                "Your password has expired. Please reset your password.",
                null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.error("Username not found: {}", ex.getMessage());
        ApiResponse<Object> error = new ApiResponse<>(
                "Error",
                "Invalid email or password",
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleInsufficientAuthenticationException(
            InsufficientAuthenticationException ex) {
        log.error("Insufficient authentication: {}", ex.getMessage());
        ApiResponse<Object> error = new ApiResponse<>(
                "Error",
                "Full authentication is required to access this resource",
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException ex) {
        log.error("Authentication failed: {}", ex.getMessage());
        ApiResponse<Object> error = new ApiResponse<>(
                "Error",
                "Authentication failed: " + ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // ==================== Spring Security Authorization Exceptions ====================

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Access denied: {}", ex.getMessage());
        ApiResponse<Object> error = new ApiResponse<>(
                "Error",
                "You don't have permission to access this resource",
                null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    // ==================== JWT Exceptions ====================

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<Object>> handleExpiredJwtException(ExpiredJwtException ex) {
        log.error("JWT token expired: {}", ex.getMessage());
        ApiResponse<Object> error = new ApiResponse<>(
                "Error",
                "Your session has expired. Please login again.",
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponse<Object>> handleMalformedJwtException(MalformedJwtException ex) {
        log.error("Malformed JWT token: {}", ex.getMessage());
        ApiResponse<Object> error = new ApiResponse<>(
                "Error",
                "Invalid token format",
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse<Object>> handleSignatureException(SignatureException ex) {
        log.error("Invalid JWT signature: {}", ex.getMessage());
        ApiResponse<Object> error = new ApiResponse<>(
                "Error",
                "Invalid token signature",
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnsupportedJwtException(UnsupportedJwtException ex) {
        log.error("Unsupported JWT token: {}", ex.getMessage());
        ApiResponse<Object> error = new ApiResponse<>(
                "Error",
                "Unsupported token format",
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage());
        ApiResponse<Object> error = new ApiResponse<>(
                "Error",
                "Invalid request: " + ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // ==================== Validation Exceptions ====================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        log.error("Validation failed: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                "Error",
                "Validation failed",
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        log.error("Constraint violation: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(violation ->
                errors.put(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                )
        );

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                "Error",
                "Validation failed",
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleTransactionSystemException(
            TransactionSystemException ex) {
        log.error("Transaction failed: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();

        // Extract constraint violations from nested exception
        Throwable cause = ex.getCause();
        if (cause instanceof jakarta.persistence.RollbackException) {
            Throwable nested = cause.getCause();
            if (nested instanceof ConstraintViolationException) {
                ConstraintViolationException cve = (ConstraintViolationException) nested;
                for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
                    errors.put(
                            violation.getPropertyPath().toString(),
                            violation.getMessage()
                    );
                }
            }
        }

        if (errors.isEmpty()) {
            errors.put("error", ex.getMessage());
        }

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                "Error",
                "Transaction failed - Validation error",
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ==================== Generic Exception Handler ====================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred: ", ex);
        ApiResponse<Object> error = new ApiResponse<>(
                "Error",
                "An unexpected error occurred. Please try again later.",
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}