package ru.subscription_manager.controller.entity.request;

import io.micrometer.common.lang.Nullable;
import ru.subscription_manager.service.entity.filter.UserFilter;

import java.util.Optional;
import java.util.UUID;

public record UserFilterRequestDto(
    @Nullable
    String firstName,
    @Nullable
    String secondName,
    @Nullable
    String subscriptionId
) {

    public UserFilter toUserFilter() {
        return new UserFilter(
                Optional.ofNullable(firstName),
                Optional.ofNullable(secondName),
                Optional.ofNullable(subscriptionId).map(UUID::fromString)
        );
    }

}
