package ru.subscription_manager.controller.entity.request.user;

import io.micrometer.common.lang.Nullable;
import ru.subscription_manager.service.entity.filter.UserFilter;

import java.util.Optional;

public record UserFilterRequestDto(
    @Nullable
    String firstName,
    @Nullable
    String secondName
) {

    public UserFilter toUserFilter() {
        return new UserFilter(
                Optional.ofNullable(firstName),
                Optional.ofNullable(secondName)
        );
    }

}
