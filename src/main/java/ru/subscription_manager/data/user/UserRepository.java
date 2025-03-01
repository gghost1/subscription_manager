package ru.subscription_manager.data.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.subscription_manager.data.type.email.Email;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Override
    @NonNull
    Optional<User> findById(@NonNull UUID uuid);

    List<User> findAllByFirstNameAndSecondName(String firstName, String secondName);

    Optional<User> findByEmail(Email email);
}
