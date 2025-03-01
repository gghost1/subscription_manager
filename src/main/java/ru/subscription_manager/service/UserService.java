package ru.subscription_manager.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.subscription_manager.configuration.exception.DbException;
import ru.subscription_manager.configuration.exception.NotFoundException;
import ru.subscription_manager.data.type.email.Email;
import ru.subscription_manager.data.user.User;
import ru.subscription_manager.data.user.UserRepository;
import ru.subscription_manager.data.user.UserSpecification;
import ru.subscription_manager.data.users_subscription.UsersSubscription;
import ru.subscription_manager.data.users_subscription.UsersSubscriptionRepository;
import ru.subscription_manager.service.entity.create.CreateEntity;
import ru.subscription_manager.service.entity.edit.EditEntity;
import ru.subscription_manager.service.entity.filter.UserFilter;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User add(CreateEntity<User> createUser) {
        try {
            User user = createUser.create();

            return userRepository.save(user);
        } catch (Exception e) {
            throw new DbException("Server error while adding user", e);
        }
    }

    public User get(UUID id) {
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (userOpt.isEmpty()) {
                throw new NotFoundException("User not found");
            }

            return userOpt.orElseThrow();
        } catch (Exception e) {
            throw new DbException("Server error while getting user", e);
        }
    }

    public User get(Email email) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                throw new NotFoundException("User not found");
            }

            return userOpt.orElseThrow();
        } catch (Exception e) {
            throw new DbException("Server error while getting user", e);
        }
    }

    public Page<User> getAll(UserFilter filter, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Specification<User> specification = UserSpecification.filterUsers(filter);
            return userRepository.findAll(specification, pageable);
        } catch (Exception e) {
            throw new DbException("Server error while getting users", e);
        }
    }

    public User edit(EditEntity<User, UUID> editUser) {
        try {
            Optional<User> userOpt = userRepository.findById(editUser.id());
            if (userOpt.isEmpty()) {
                throw new NotFoundException("User for edit not found");
            }

            User edited = editUser.edit(userOpt.orElseThrow());

            return userRepository.save(edited);
        } catch (Exception e) {
            throw new DbException("Server error while editing user", e);
        }
    }

    public void delete(UUID id) {
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (userOpt.isEmpty()) {
                throw new NotFoundException("User for delete not found");
            }
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DbException("Server error while deleting user", e);
        }
    }

}