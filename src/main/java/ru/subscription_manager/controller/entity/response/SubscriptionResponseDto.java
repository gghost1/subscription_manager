package ru.subscription_manager.controller.entity.response;

import ru.subscription_manager.data.subscription.Subscription;

import java.util.UUID;

public record SubscriptionResponseDto(
    UUID id,
    String name
) {

    public static SubscriptionResponseDto fromSubscription(Subscription subscription) {
        return new SubscriptionResponseDto(
                subscription.getId(),
                subscription.getName()
        );
    }

}
