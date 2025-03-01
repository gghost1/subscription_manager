package ru.subscription_manager.service.entity.filter;

import java.util.Optional;

public record UsersSubscriptionFilter(
        Optional<ExpirationDateFilter> expirationDate,
        Optional<Boolean> active
) {
}
