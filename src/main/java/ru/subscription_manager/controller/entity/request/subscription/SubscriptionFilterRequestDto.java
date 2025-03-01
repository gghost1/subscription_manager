package ru.subscription_manager.controller.entity.request.subscription;

import ru.subscription_manager.service.entity.filter.SubscriptionFilter;

import java.util.Optional;
import java.util.UUID;

public record SubscriptionFilterRequestDto(
        String userId
) {

    public SubscriptionFilter toSubscriptionFilter() {
        return new SubscriptionFilter(Optional.ofNullable(userId).map(UUID::fromString));
    }

}
