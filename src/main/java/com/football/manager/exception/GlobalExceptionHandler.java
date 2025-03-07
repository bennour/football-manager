package com.football.manager.exception;

import com.football.manager.model.dto.ErrorDto;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler to manage application exceptions and provide meaningful HTTP responses.
 * This class catches various exceptions and returns appropriate HTTP status codes along with error messages.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        logger.warn(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.warn(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFundsException(InsufficientFundsException ex) {
        logger.warn(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorDto.Error> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorDto.Error(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        logger.warn(errors);

        return new ResponseEntity<>(new ErrorDto(errors), HttpStatus.BAD_REQUEST);
    }
}

