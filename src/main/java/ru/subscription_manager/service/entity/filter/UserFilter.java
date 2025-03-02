package ru.subscription_manager.service.entity.filter;

import java.util.Optional;
import java.util.UUID;

/**
 * Entity to filter {@link ru.subscription_manager.data.user.User}
 * @param firstName optional filter by first name
 * @param secondName optional filter by second name
 * @param subscriptionId optional filter by subscription id
 */
public record UserFilter(
        Optional<String> firstName,
        Optional<String> secondName,
        Optional<UUID> subscriptionId
) {
}
