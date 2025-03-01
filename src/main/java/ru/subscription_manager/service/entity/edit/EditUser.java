package ru.subscription_manager.service.entity.edit;

import ru.subscription_manager.data.type.email.Email;
import ru.subscription_manager.data.type.user_name.UserName;
import ru.subscription_manager.data.user.User;

import java.util.Optional;
import java.util.UUID;

public record EditUser(
        UUID id,
        Optional<String> firstName,
        Optional<String> secondName,
        Optional<String> lastName,
        Optional<String> email
) implements EditEntity<User, UUID> {

    @Override
    public User edit(User user) {
        return new User(
                user.getId(),
                new UserName(
                        firstName.orElse(user.getName().getFirstName()),
                        secondName.orElse(user.getName().getSecondName()),
                        lastName.orElse(user.getName().getLastName())
                ),
                email.map(Email::new).orElse(user.getEmail()),
                user.getFirstName(),
                user.getSecondName(),
                user.getUserSubscriptions()
        );
    }
}
