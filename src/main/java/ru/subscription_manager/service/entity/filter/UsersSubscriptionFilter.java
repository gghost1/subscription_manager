package ru.subscription_manager.service.entity.filter;

import java.util.Optional;

/**
 * Entity to filter {@link ru.subscription_manager.data.users_subscription.UsersSubscription}
 * @param expirationDate optional filter by expiration date,
 * @param active optional filter by active subscription
 */
public record UsersSubscriptionFilter(
        Optional<ExpirationDateFilter> expirationDate,
        Optional<Boolean> active
) {
}
