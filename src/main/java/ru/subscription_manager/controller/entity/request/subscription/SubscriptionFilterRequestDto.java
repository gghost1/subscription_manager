package ru.subscription_manager.controller.entity.request.subscription;

import jakarta.annotation.Nullable;
import ru.subscription_manager.service.entity.filter.SubscriptionFilter;

import java.util.Optional;
import java.util.UUID;

public record SubscriptionFilterRequestDto(
        @Nullable
        String userId
) {

    public SubscriptionFilter toSubscriptionFilter() {
        return new SubscriptionFilter(Optional.ofNullable(userId).map(UUID::fromString));
    }

}
