package ru.subscription_manager.data.users_subscription;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private UsersSubscriptionId id;

    @Setter
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @ManyToOne
    @MapsId("subscriptionId")
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "active")
    private boolean active;

    @Column(name = "createdAt", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDate updatedAt;

    public UsersSubscription(
            UsersSubscriptionId id,
            LocalDate expirationDate,
            boolean active,
            LocalDate createdAt
    ) {
        this.id = id;
        this.expirationDate = expirationDate;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = LocalDate.now();
    }

}