package ru.subscription_manager.service.entity.filter;

import java.time.LocalDate;

/**
 * Entity to filter {@link ru.subscription_manager.data.users_subscription.UsersSubscription}
 * @param expirationDate expiration date
 * @param comparison comparison type
 * Both fields are required
 */
public record ExpirationDateFilter(
        LocalDate expirationDate,
        ComparisonType comparison
){
}