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
import ru.subscription_manager.data.subscription.Subscription;
import ru.subscription_manager.data.subscription.SubscriptionRepository;
import ru.subscription_manager.data.user.User;
import ru.subscription_manager.data.user.UserRepository;
import ru.subscription_manager.data.users_subscription.UsersSubscription;
import ru.subscription_manager.data.users_subscription.UsersSubscriptionId;
import ru.subscription_manager.data.users_subscription.UsersSubscriptionRepository;
import ru.subscription_manager.data.users_subscription.UsersSubscriptionSpecification;
import ru.subscription_manager.service.entity.create.CreateEntity;
import ru.subscription_manager.service.entity.edit.EditEntity;
import ru.subscription_manager.service.entity.filter.UsersSubscriptionFilter;

import java.util.Optional;
import java.util.UUID;

/**
 * Manages associations between users and subscriptions. Handles creation,
 * modification, and deletion of user-subscription relationships. All
 * operations are transactional and enforce referential integrity.
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UsersSubscriptionService {

    private final UsersSubscriptionRepository usersSubscriptionRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    /**
     * Creates a new user-subscription association after validating both entities exist.
     *
     * @param createUsersSubscription container with association data {@link ru.subscription_manager.service.entity.create.CreateUsersSubscription}
     * @return persisted user-subscription relationship
     * @throws DbException if persistence operation fails
     */
    public UsersSubscription add(CreateEntity<UsersSubscription> createUsersSubscription) {
        try {
            UsersSubscription usersSubscription = createUsersSubscription.create();

            Optional<User> userOpt = userRepository.findById(usersSubscription.getId().userId());
            if (userOpt.isEmpty()) {
                throw new NotFoundException("User not found");
            }

            Optional<Subscription> subscriptionOpt = subscriptionRepository
                    .findById(usersSubscription.getId().subscriptionId());
            if (subscriptionOpt.isEmpty()) {
                throw new NotFoundException("Subscription not found");
            }

            usersSubscription.setUser(userOpt.orElseThrow());
            usersSubscription.setSubscription(subscriptionOpt.orElseThrow());

            return usersSubscriptionRepository.save(usersSubscription);
        } catch (Exception e) {
            throw new DbException("Server error while adding users subscription", e);
        }
    }

    /**
     * Retrieves paginated list of user-subscription associations with filtering.
     *
     * @param filter search criteria filtering criteria {@link UsersSubscriptionFilter}
     * @param userId user ID for relationship filtering
     * @param page zero-based page index
     * @param size number of items per page
     * @return page of matching user-subscription relationships
     * @throws DbException if data retrieval fails
     */
    public Page<UsersSubscription> getAll(UsersSubscriptionFilter filter, UUID userId, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
            Specification<UsersSubscription> specification = UsersSubscriptionSpecification
                    .filterUsersSubscriptions(filter, userId);
            return usersSubscriptionRepository.findAll(specification, pageable);
        } catch (Exception e) {
            throw new DbException("Server error while getting users subscriptions", e);
        }
    }

    /**
     * Updates existing user-subscription relationship properties.
     *
     * @param editUsersSubscription container with modified relationship data {@link ru.subscription_manager.service.entity.edit.EditUsersSubscription}
     * @return updated user-subscription entity
     * @throws DbException if update operation fails
     */
    public UsersSubscription edit(EditEntity<UsersSubscription, UsersSubscriptionId> editUsersSubscription) {
        try {
            Optional<UsersSubscription> usersSubscriptionOpt = usersSubscriptionRepository
                    .findById(editUsersSubscription.id());
            if (usersSubscriptionOpt.isEmpty()) {
                throw new NotFoundException("Users subscription for edit not found");
            }

            UsersSubscription usersSubscriptionEdited = editUsersSubscription
                    .edit(usersSubscriptionOpt.orElseThrow());

            return usersSubscriptionRepository.save(usersSubscriptionEdited);
        } catch (Exception e) {
            throw new DbException("Server error while editing users subscription", e);
        }
    }

    /**
     * Removes user-subscription relationship by composite ID.
     *
     * @param id composite identifier {@link UsersSubscriptionId}
     * @throws DbException if deletion operation fails
     */
    public void delete(UsersSubscriptionId id) {
        try {
            usersSubscriptionRepository.deleteById(id);
        } catch (Exception e) {
            throw new DbException("Server error while deleting users subscription", e);
        }
    }

}
