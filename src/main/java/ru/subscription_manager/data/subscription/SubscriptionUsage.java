package ru.subscription_manager.data.subscription;

import java.util.UUID;

public record SubscriptionUsage(
    UUID subscriptionId,
    String name,
    long userCount
) {

    /**
     * Builds a SubscriptionUsage object from a row of data
     * @param row array of row data
     * @return SubscriptionUsage
     */
    public static SubscriptionUsage from(Object[] row) {
        return new SubscriptionUsage(
                (UUID) row[0],
                (String) row[1],
                ((Number) row[2]).longValue()
        );
    }

}
