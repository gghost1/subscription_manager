package ru.subscription_manager.controller.entity.request.subscription;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.subscription_manager.service.entity.create.CreateSubscription;

public record CreateSubscriptionRequestDto(
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    String name,
    @NotNull(message = "Period is required")
    @Min(value = 1, message = "Period must be greater than 0")
    Integer period
) {

    public CreateSubscription toCreateSubscription() {
        return new CreateSubscription(name);
    }

}
