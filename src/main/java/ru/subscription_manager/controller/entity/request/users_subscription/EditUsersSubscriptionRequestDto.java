package ru.subscription_manager.controller.entity.request.users_subscription;

import ru.subscription_manager.data.users_subscription.UserSubscriptionId;
import ru.subscription_manager.service.entity.edit.EditUsersSubscription;

import java.time.LocalDate;
import java.util.Optional;

public record EditUsersSubscriptionRequestDto(
        LocalDate expirationDate,
        Boolean active
) {

    public EditUsersSubscription toEditUsersSubscription(UserSubscriptionId userSubscriptionId) {
        return new EditUsersSubscription(
                userSubscriptionId,
                Optional.ofNullable(expirationDate),
                Optional.ofNullable(active)
        );
    }

}
