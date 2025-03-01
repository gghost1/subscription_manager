package ru.subscription_manager.service.entity.create;

import ru.subscription_manager.data.users_subscription.UserSubscriptionId;
import ru.subscription_manager.data.users_subscription.UsersSubscription;

import java.time.LocalDate;
import java.util.UUID;

public record CreateUsersSubscription(
    UUID userId,
    UUID subscriptionId,
    LocalDate expirationDate,
    boolean active
) implements CreateEntity<UsersSubscription> {

    @Override
    public UsersSubscription create() {
        return new UsersSubscription(
            new UserSubscriptionId(userId, subscriptionId),
            expirationDate,
            active
        );
    }
}
