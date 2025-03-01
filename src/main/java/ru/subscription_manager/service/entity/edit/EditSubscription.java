package ru.subscription_manager.service.entity.edit;

import ru.subscription_manager.data.subscription.Subscription;

import java.util.Optional;
import java.util.UUID;

public record EditSubscription(
        UUID id,
        Optional<String> name
) implements EditEntity<Subscription, UUID> {

    @Override
    public Subscription edit(Subscription entity) {
        return new Subscription(
                entity.getId(),
                name.orElse(entity.getName()),
                entity.getUserSubscriptions()
        );
    }
}
