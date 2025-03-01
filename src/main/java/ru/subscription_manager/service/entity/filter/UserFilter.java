package ru.subscription_manager.service.entity.filter;

import java.util.Optional;
import java.util.UUID;

public record UserFilter(
        Optional<String> firstName,
        Optional<String> secondName,
        Optional<UUID> subscriptionId
) {
}
