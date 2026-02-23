package com.br.stockpro.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ErrorResponse buildError(
            HttpStatus status,
            String message,
            String path,
            List<String> errors
    ) {
        return new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                Instant.now(),
                errors
        );
    }

    // 🔹 404 - Recurso não encontrado
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return buildError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    // 🔹 400 - Regra de negócio
    @ExceptionHandler(BusinessException.class)
    public ErrorResponse handleBusiness(BusinessException ex, HttpServletRequest request) {
        return buildError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    // 🔹 400 - Validação @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return buildError(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                request.getRequestURI(),
                errors
        );
    }

    // 🔹 400 - ConstraintViolation
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {

        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .toList();

        return buildError(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                request.getRequestURI(),
                errors
        );
    }

    // 🔹 400 - JSON inválido
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse handleInvalidJson(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return buildError(
                HttpStatus.BAD_REQUEST,
                "JSON inválido ou mal formatado.",
                request.getRequestURI(),
                null
        );
    }

    // 🔹 409 - Violação de integridade (ex: CNPJ duplicado)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {

        return buildError(
                HttpStatus.CONFLICT,
                "Violação de integridade de dados.",
                request.getRequestURI(),
                null
        );
    }

    // 🔹 500 - Erro inesperado
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGeneric(Exception ex, HttpServletRequest request) {

        log.error("Erro inesperado:", ex);

        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno no servidor.",
                request.getRequestURI(),
                null
        );
    }
}
