package ru.subscription_manager.controller.entity.response;

import ru.subscription_manager.data.users_subscription.UsersSubscription;

import java.time.LocalDate;

public record UsersSubscriptionResponseDto(
        UserResponseDto user,
        SubscriptionResponseDto subscription,
        boolean active,
        LocalDate expirationDate
) {

    public static UsersSubscriptionResponseDto fromUsersSubscription(UsersSubscription usersSubscription) {
        return new UsersSubscriptionResponseDto(
                UserResponseDto.fromUser(usersSubscription.getUser()),
                SubscriptionResponseDto.fromSubscription(usersSubscription.getSubscription()),
                usersSubscription.isActive(),
                usersSubscription.getExpirationDate()
        );
    }

}
