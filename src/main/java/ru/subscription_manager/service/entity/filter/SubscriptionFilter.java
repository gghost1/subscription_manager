package ru.subscription_manager.service.entity.filter;

import java.util.Optional;
import java.util.UUID;

/**
 * Entity to filter {@link ru.subscription_manager.data.subscription.Subscription}
 * @param user optional filter by user id
 */
public record SubscriptionFilter(
        Optional<UUID> user
) {
}
