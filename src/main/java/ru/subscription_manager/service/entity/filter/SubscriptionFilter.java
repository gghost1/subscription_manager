package ru.subscription_manager.service.entity.filter;

import java.util.Optional;

/**
 * Entity to filter {@link ru.subscription_manager.data.subscription.Subscription}
 * @param name optional filter by name
 */
public record SubscriptionFilter(
        Optional<String> name
) {
}
