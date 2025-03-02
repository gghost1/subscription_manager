package ru.subscription_manager.controller.entity.response;

public record ExceptionResponseDto(
        String message,
        String exceptionClass
) {
}
