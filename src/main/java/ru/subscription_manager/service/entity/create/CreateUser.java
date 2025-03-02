package ru.subscription_manager.service.entity.create;

import ru.subscription_manager.data.type.email.Email;
import ru.subscription_manager.data.type.user_name.UserName;
import ru.subscription_manager.data.user.User;

import java.time.LocalDate;

public record CreateUser(
        String firstName,
        String secondName,
        String lastName,
        String email
) implements CreateEntity<User> {

    @Override
    public User create() {
        return new User(
                new UserName(firstName, secondName, lastName),
                new Email(email),
                LocalDate.now()
        );
    }

}
