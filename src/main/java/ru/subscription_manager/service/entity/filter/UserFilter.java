package ru.subscription_manager.service.entity.filter;

import java.util.Optional;
import java.util.UUID;

/**
 * Entity to filter {@link ru.subscription_manager.data.user.User}
 * @param firstName optional filter by first name
 * @param secondName optional filter by second name
 */
public record UserFilter(
        Optional<String> firstName,
        Optional<String> secondName
) {
}
