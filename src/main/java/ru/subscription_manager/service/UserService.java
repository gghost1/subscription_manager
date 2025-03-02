package ru.subscription_manager.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.subscription_manager.configuration.exception.DbException;
import ru.subscription_manager.configuration.exception.NotFoundException;
import ru.subscription_manager.data.type.email.Email;
import ru.subscription_manager.data.user.User;
import ru.subscription_manager.data.user.UserRepository;
import ru.subscription_manager.data.user.UserSpecification;
import ru.subscription_manager.service.entity.create.CreateEntity;
import ru.subscription_manager.service.entity.edit.EditEntity;
import ru.subscription_manager.service.entity.filter.UserFilter;

import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing users. Provides basic CRUD operations,
 * filtering and pagination support. All methods execute within transactional context.
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /**
     * Creates a new user from provided data.
     *
     * @param createUser data container for user creation {@link ru.subscription_manager.service.entity.create.CreateUser}
     * @return persisted user entity
     * @throws DbException if persistence operation fails
     */
    public User add(CreateEntity<User> createUser) {
        try {
            User user = createUser.create();

            return userRepository.save(user);
        } catch (Exception e) {
            throw new DbException("Server error while adding user", e);
        }
    }

    /**
     * Retrieves user by unique identifier.
     *
     * @param id user's UUID
     * @return found user entity
     * @throws DbException if data retrieval error occurs
     */
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

    /**
     * Finds user by email address.
     *
     * @param email Email object for search {@link ru.subscription_manager.data.type.email.Email}
     * @return matching user entity
     * @throws DbException if data retrieval error occurs
     */
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

    /**
     * Returns paginated list of users filtered by specified criteria.
     *
     * @param filter user filtering criteria {@link UserFilter}
     * @param page zero-based page index
     * @param size number of items per page
     * @return page of users matching the filter
     * @throws DbException if data retrieval error occurs
     */
    public Page<User> getAll(UserFilter filter, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
            Specification<User> specification = UserSpecification.filterUsers(filter);
            return userRepository.findAll(specification, pageable);
        } catch (Exception e) {
            throw new DbException("Server error while getting users", e);
        }
    }

    /**
     * Updates existing user.
     *
     * @param editUser data container for user modification {@link ru.subscription_manager.service.entity.edit.EditUser}
     * @return updated user entity
     * @throws DbException if data update operation fails
     */
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

    /**
     * Deletes user by unique identifier.
     *
     * @param id UUID of user to delete
     * @throws DbException if data deletion error occurs
     */
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