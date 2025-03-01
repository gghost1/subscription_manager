package ru.subscription_manager.controller.entity.response;

import ru.subscription_manager.data.subscription.SubscriptionUsage;

import java.util.UUID;

public record SubscriptionUsageResponseDto(
    UUID id,
    String name,
    long userCount
) {

    public static SubscriptionUsageResponseDto fromSubscriptionUsage(SubscriptionUsage subscriptionUsage) {
        return new SubscriptionUsageResponseDto(
                subscriptionUsage.subscriptionId(),
                subscriptionUsage.name(),
                subscriptionUsage.userCount()
        );
    }

}
