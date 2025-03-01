package ru.subscription_manager.data.users_subscription;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.subscription_manager.data.subscription.Subscription;
import ru.subscription_manager.data.user.User;

import java.time.LocalDate;

@Entity
@Table(name = "users_subscriptions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsersSubscription {

    @EmbeddedId
    private UserSubscriptionId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("subscriptionId")
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "active")
    private boolean active;
}