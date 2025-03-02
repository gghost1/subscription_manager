package ru.subscription_manager.controller.entity.response;

import ru.subscription_manager.data.user.User;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponseDto(
    UUID id,
    String firstName,
    String secondName,
    String lastName,
    String email,
    LocalDate createdAt,
    LocalDate updatedAt
) {

    public static UserResponseDto fromUser(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName().getFirstName(),
                user.getName().getSecondName(),
                user.getName().getLastName(),
                user.getEmail().value(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

}
