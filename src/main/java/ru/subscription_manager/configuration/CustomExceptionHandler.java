package ru.subscription_manager.configuration;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.subscription_manager.configuration.exception.DbException;
import ru.subscription_manager.configuration.exception.NotFoundException;
import ru.subscription_manager.controller.entity.response.ExceptionResponseDto;

import java.time.LocalDate;

@Slf4j
@Hidden
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(DbException.class)
    public ResponseEntity<ExceptionResponseDto> handleDbException(DbException ex) {
        if (ex.getException() instanceof NotFoundException) {
            return ResponseEntity.status(404).body(new ExceptionResponseDto(
                    ex.getException().getMessage(),
                    "404")
            );
        }
        log.error("{} Db exception: {}", LocalDate.now(), ex.getMessage(), ex.getException());
        return ResponseEntity.status(500).body(new ExceptionResponseDto(
                "Internal server error",
                "500")
        );
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            TypeMismatchException.class,
            IllegalArgumentException.class,
            HttpMessageNotReadableException.class,
            InvalidDataAccessApiUsageException.class
    })
    public ResponseEntity<ExceptionResponseDto> handleValidationException(RuntimeException ex) {
        return ResponseEntity.status(400).body(new ExceptionResponseDto(
                ex.getMessage(),
                "400")
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        if (ex.getMessage().contains("duplicate") && ex.getMessage().contains("Key (email)")) {
            return ResponseEntity.status(400).body(new ExceptionResponseDto(
                    "Email already exists",
                    "400")
            );
        } else if (ex.getMessage().contains("only_letters")) {
            return ResponseEntity.status(400).body(new ExceptionResponseDto(
                    "Only letters are allowed",
                    "400")
            );
        } else {
            log.error("{} Data integrity violation exception: {}", LocalDate.now(), ex.getMessage(), ex);
            return ResponseEntity.status(500).body(new ExceptionResponseDto(
                    "Internal server error",
                    "500")
            );
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleException(Exception ex) {
        log.error("{} Exception: {}", LocalDate.now(), ex.getMessage(), ex);
        return ResponseEntity.status(500).body(new ExceptionResponseDto(
                "Internal server error",
                "500")
        );
    }

}
