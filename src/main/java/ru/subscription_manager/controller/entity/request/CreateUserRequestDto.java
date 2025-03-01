package ru.subscription_manager.controller.entity.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.subscription_manager.service.entity.create.CreateUser;

public record CreateUserRequestDto(
    @NotNull(message = "First name is required")
    @NotBlank(message = "First name is required")
    String firstName,
    @NotNull(message = "Second name is required")
    @NotBlank(message = "Second name is required")
    String secondName,
    @Nullable
    String lastName,
    @NotNull(message = "Email is required")
    @NotBlank(message = "Email is required")
    String email
) {

    public CreateUser toCreateUser() {
        return new CreateUser(
                firstName,
                secondName,
                lastName,
                email
        );
    }

}
