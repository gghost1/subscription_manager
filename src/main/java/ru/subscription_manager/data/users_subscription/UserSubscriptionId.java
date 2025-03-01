package ru.subscription_manager.data.users_subscription;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record UserSubscriptionId(
        @Column(name = "user_id")
        UUID userId,
        @Column(name = "subscription_id")
        UUID subscriptionId
) implements Serializable {
}
