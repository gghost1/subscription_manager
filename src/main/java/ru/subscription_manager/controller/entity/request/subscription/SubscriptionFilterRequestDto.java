package ru.subscription_manager.controller.entity.request.subscription;

import jakarta.annotation.Nullable;
import ru.subscription_manager.service.entity.filter.SubscriptionFilter;

import java.util.Optional;

public record SubscriptionFilterRequestDto(
        @Nullable
        String name
) {

    public SubscriptionFilter toSubscriptionFilter() {
        return new SubscriptionFilter(Optional.ofNullable(name));
    }

}
