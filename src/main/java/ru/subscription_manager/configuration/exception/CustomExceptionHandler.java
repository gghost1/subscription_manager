package ru.subscription_manager.configuration.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.subscription_manager.controller.entity.response.ExceptionResponseDto;

import java.time.LocalDate;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(DbException.class)
    public ResponseEntity<ExceptionResponseDto> handleDbException(DbException ex) {
        if (ex.getException() instanceof NotFoundException) {
            return ResponseEntity.status(404).body(new ExceptionResponseDto(
                    ex.getException().getMessage(),
                    ex.getException().getClass().getSimpleName())
            );
        }
        log.error("{} Db exception: {}", LocalDate.now(), ex.getMessage(), ex.getException());
        return ResponseEntity.status(500).body(new ExceptionResponseDto(
                "Internal server error",
                ex.getException().getClass().getSimpleName())
        );
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            TypeMismatchException.class,
            IllegalArgumentException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ExceptionResponseDto> handleValidationException(RuntimeException ex) {
        return ResponseEntity.status(400).body(new ExceptionResponseDto(
                ex.getMessage(),
                ex.getClass().getSimpleName())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleException(Exception ex) {
        log.error("{} Exception: {}", LocalDate.now(), ex.getMessage(), ex);
        return ResponseEntity.status(500).body(new ExceptionResponseDto(
                "Internal server error",
                ex.getClass().getSimpleName())
        );
    }

}
