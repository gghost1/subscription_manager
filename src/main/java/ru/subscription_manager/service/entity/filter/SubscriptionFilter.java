package ru.subscription_manager.service.entity.filter;

import java.util.Optional;
import java.util.UUID;

public record SubscriptionFilter(
        Optional<UUID> user
) {
}
