package ru.subscription_manager.service.entity.edit;

import ru.subscription_manager.data.users_subscription.UsersSubscriptionId;
import ru.subscription_manager.data.users_subscription.UsersSubscription;

import java.time.LocalDate;
import java.util.Optional;

public record EditUsersSubscription(
        UsersSubscriptionId id,
        Optional<LocalDate> expirationDate,
        Optional<Boolean> active
) implements EditEntity<UsersSubscription, UsersSubscriptionId> {

    @Override
    public UsersSubscription edit(UsersSubscription entity) {
        return new UsersSubscription(
                entity.getId(),
                entity.getUser(),
                entity.getSubscription(),
                expirationDate.orElse(entity.getExpirationDate()),
                active.orElse(entity.isActive())
        );
    }
}
