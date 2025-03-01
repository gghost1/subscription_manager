package ru.subscription_manager.controller.entity.request.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import ru.subscription_manager.service.entity.edit.EditUser;

import java.util.Optional;
import java.util.UUID;

public record EditUserRequestDto(
        @NotNull(message = "Id is required")
        String id,
        @Nullable
        String firstName,
        @Nullable
        String secondName,
        @Nullable
        String lastName,
        @Nullable
        String email
) {

    public EditUser toEditUser() {
        return new EditUser(
                UUID.fromString(id),
                Optional.ofNullable(firstName),
                Optional.ofNullable(secondName),
                Optional.ofNullable(lastName),
                Optional.ofNullable(email)
        );
    }

}
