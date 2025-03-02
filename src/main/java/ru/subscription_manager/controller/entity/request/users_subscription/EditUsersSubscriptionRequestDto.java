package ru.subscription_manager.controller.entity.request.users_subscription;

import jakarta.annotation.Nullable;
import ru.subscription_manager.data.users_subscription.UsersSubscriptionId;
import ru.subscription_manager.service.entity.edit.EditUsersSubscription;

import java.time.LocalDate;
import java.util.Optional;

public record EditUsersSubscriptionRequestDto(
        @Nullable
        LocalDate expirationDate,
        @Nullable
        Boolean active
) {

    public EditUsersSubscription toEditUsersSubscription(UsersSubscriptionId usersSubscriptionId) {
        return new EditUsersSubscription(
                usersSubscriptionId,
                Optional.ofNullable(expirationDate),
                Optional.ofNullable(active)
        );
    }

}
