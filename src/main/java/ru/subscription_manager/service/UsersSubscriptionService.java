package ru.subscription_manager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.subscription_manager.configuration.exception.DbException;
import ru.subscription_manager.configuration.exception.NotFoundException;
import ru.subscription_manager.data.subscription.Subscription;
import ru.subscription_manager.data.subscription.SubscriptionRepository;
import ru.subscription_manager.data.user.User;
import ru.subscription_manager.data.user.UserRepository;
import ru.subscription_manager.data.users_subscription.UserSubscriptionId;
import ru.subscription_manager.data.users_subscription.UsersSubscription;
import ru.subscription_manager.data.users_subscription.UsersSubscriptionRepository;
import ru.subscription_manager.data.users_subscription.UsersSubscriptionSpecification;
import ru.subscription_manager.service.entity.create.CreateEntity;
import ru.subscription_manager.service.entity.edit.EditEntity;
import ru.subscription_manager.service.entity.filter.UsersSubscriptionFilter;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UsersSubscriptionService {

    private final UsersSubscriptionRepository usersSubscriptionRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public UsersSubscription add(CreateEntity<UsersSubscription> createUsersSubscription) {
        try {
            UsersSubscription usersSubscription = createUsersSubscription.create();

            Optional<User> userOpt = userRepository.findById(usersSubscription.getId().userId());
            if (userOpt.isEmpty()) {
                throw new NotFoundException("User not found");
            }

            Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(usersSubscription.getId().subscriptionId());
            if (subscriptionOpt.isEmpty()) {
                throw new NotFoundException("Subscription not found");
            }

            usersSubscription.setUser(userOpt.orElseThrow());
            usersSubscription.setSubscription(subscriptionOpt.orElseThrow());

            return usersSubscriptionRepository.save(usersSubscription);
        } catch (Exception e) {
            log.error("", e);

            throw new DbException("Server error while adding users subscription", e);
        }
    }

    public Page<UsersSubscription> getAll(UsersSubscriptionFilter filter, UUID id, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Specification<UsersSubscription> specification = UsersSubscriptionSpecification.filterUsersSubscriptions(filter, id);
            return usersSubscriptionRepository.findAll(specification, pageable);
        } catch (Exception e) {
            throw new DbException("Server error while getting users subscriptions", e);
        }
    }

    public UsersSubscription edit(EditEntity<UsersSubscription, UserSubscriptionId> editUsersSubscription) {
        try {
            Optional<UsersSubscription> usersSubscriptionOpt = usersSubscriptionRepository.findById(editUsersSubscription.id());
            if (usersSubscriptionOpt.isEmpty()) {
                throw new NotFoundException("Users subscription for edit not found");
            }

            UsersSubscription usersSubscriptionEdited = editUsersSubscription.edit(usersSubscriptionOpt.orElseThrow());

            return usersSubscriptionRepository.save(usersSubscriptionEdited);
        } catch (Exception e) {
            throw new DbException("Server error while editing users subscription", e);
        }
    }

    public void delete(UserSubscriptionId id) {
        try {
            usersSubscriptionRepository.deleteById(id);
        } catch (Exception e) {
            throw new DbException("Server error while deleting users subscription", e);
        }
    }

}
