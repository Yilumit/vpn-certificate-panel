package com.vpnpanel.VpnPanel.adapters.exceptions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vpnpanel.VpnPanel.domain.exceptions.ValidatePasswordException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex, request);
    }

    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<ErrorResponse> handleEmailSending(EmailSendingException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex, request);
    }

    @ExceptionHandler(ValidatePasswordException.class)
    public ResponseEntity<ErrorResponse> handleValidatePassword(ValidatePasswordException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), ex, request);
    }

    @ExceptionHandler(CertificateFileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCertificateFileNotFound(CertificateFileNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), ex, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<FieldErrorResponse> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
            .map( err -> new FieldErrorResponse(err.getField(), err.getDefaultMessage()))
            .collect(Collectors.toList());

        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            ex.getMessage(),
            request.getRequestURI(),
            fieldErrors
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Erro inesperado: {}", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex, request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus badRequest, String message,
            Exception ex, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            badRequest.value(),
            badRequest.getReasonPhrase(),
            message,
            request.getRequestURI(),
            null
        );

        return new ResponseEntity<>(response, badRequest);
    }
}
