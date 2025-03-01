package ru.subscription_manager.data.subscription;

import java.util.UUID;

public record SubscriptionUsage(
    UUID subscriptionId,
    String name,
    long userCount
) {

    public static SubscriptionUsage from(Object[] row) {
        return new SubscriptionUsage(
                (UUID) row[0],
                (String) row[1],
                ((Number) row[2]).longValue()
        );
    }

}
