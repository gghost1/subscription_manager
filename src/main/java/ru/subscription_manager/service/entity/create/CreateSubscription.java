package ru.subscription_manager.service.entity.create;

import ru.subscription_manager.data.subscription.Subscription;

public record CreateSubscription(
        String name
) implements CreateEntity<Subscription> {

    @Override
    public Subscription create() {
        return new Subscription(name);
    }
}
