package ru.subscription_manager.controller.entity.request.subscription;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.subscription_manager.service.entity.edit.EditSubscription;

import java.util.Optional;
import java.util.UUID;

public record EditSubscriptionRequestDto(
    @NotNull(message = "Id is required")
    @NotBlank(message = "Id is required")
    String id,
    @Nullable
    String name
) {

    public EditSubscription toEditSubscription() {
        return new EditSubscription(
                UUID.fromString(id),
                Optional.ofNullable(name)
        );
    }

}
