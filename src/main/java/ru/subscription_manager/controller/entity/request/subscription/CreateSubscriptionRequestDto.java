package ru.subscription_manager.controller.entity.request.subscription;

import ru.subscription_manager.service.entity.create.CreateSubscription;

public record CreateSubscriptionRequestDto(
    String name,
    int period
) {

    public CreateSubscription toCreateSubscription() {
        return new CreateSubscription(name);
    }

}
