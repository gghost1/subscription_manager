package ru.subscription_manager.controller.entity.request.users_subscription;

import jakarta.annotation.Nullable;
import ru.subscription_manager.service.entity.filter.ComparisonType;
import ru.subscription_manager.service.entity.filter.ExpirationDateFilter;
import ru.subscription_manager.service.entity.filter.UsersSubscriptionFilter;

import java.time.LocalDate;
import java.util.Optional;

public record UsersSubscriptionFilterRequestDto(
        @Nullable
        ComparisonType comparisonType,
        @Nullable
        LocalDate expirationDate,
        @Nullable
        Boolean active
) {

    public UsersSubscriptionFilter toUsersSubscriptionFilter() {
        if (expirationDate == null || comparisonType == null) {
            return new UsersSubscriptionFilter(
                    Optional.empty(),
                    Optional.ofNullable(active)
            );
        }
        return new UsersSubscriptionFilter(
                Optional.of(new ExpirationDateFilter(expirationDate, comparisonType)),
                Optional.ofNullable(active)
        );
    }

}
