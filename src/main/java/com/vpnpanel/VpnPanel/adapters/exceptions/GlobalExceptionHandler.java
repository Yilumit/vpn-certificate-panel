package com.vpnpanel.VpnPanel.adapters.exceptions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Requisição com argumentos inválidos", ex, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Credenciais inválidas", ex, request);
    }

    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<ErrorResponse> handleEmailSending(EmailSendingException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao enviar o e-mail", ex, request);
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
            "Erro de validação nos campos enviados",
            request.getRequestURI(),
            fieldErrors
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Erro inesperado: {}", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor", ex, request);
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
