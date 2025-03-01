package ru.subscription_manager.controller.entity.request.subscription;

import ru.subscription_manager.service.entity.edit.EditSubscription;

import java.util.Optional;
import java.util.UUID;

public record EditSubscriptionRequestDto(
    String id,
    String name
) {

    public EditSubscription toEditSubscription() {
        return new EditSubscription(
                UUID.fromString(id),
                Optional.ofNullable(name)
        );
    }

}
