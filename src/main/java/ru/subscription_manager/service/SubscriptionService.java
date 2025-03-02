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
import ru.subscription_manager.data.subscription.Subscription;
import ru.subscription_manager.data.subscription.SubscriptionRepository;
import ru.subscription_manager.data.subscription.SubscriptionSpecification;
import ru.subscription_manager.data.subscription.SubscriptionUsage;
import ru.subscription_manager.service.entity.create.CreateEntity;
import ru.subscription_manager.service.entity.edit.EditEntity;
import ru.subscription_manager.service.entity.filter.SubscriptionFilter;
import ru.subscription_manager.service.entity.filter.UserFilter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing subscription entities. Handles CRUD operations,
 * popularity statistics, and filtered pagination. Executes all methods
 * within transactional boundaries.
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    /**
     * Creates or returns existing subscription based on name uniqueness.
     *
     * @param createSubscription container with subscription data {@link ru.subscription_manager.service.entity.create.CreateSubscription}
     * @return existing or newly created subscription
     * @throws DbException if persistence operation fails
     */
    public Subscription add(CreateEntity<Subscription> createSubscription) {
        try {
            Subscription subscription = createSubscription.create();

            Optional<Subscription> subscriptionOpt = subscriptionRepository
                    .findByName(subscription.getName());
            if (subscriptionOpt.isPresent()) {
                return subscriptionOpt.orElseThrow();
            }

            return subscriptionRepository.save(subscription);
        } catch (Exception e) {
            throw new DbException("Server error while adding subscription", e);
        }
    }

    /**
     * Retrieves subscription by unique identifier.
     *
     * @param id subscription UUID
     * @return found subscription entity
     * @throws DbException if data retrieval fails
     */
    public Subscription get(UUID id) {
        try {
            Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(id);
            if (subscriptionOpt.isEmpty()) {
                throw new NotFoundException("Subscription not found");
            }

            return subscriptionOpt.orElseThrow();
        } catch (Exception e) {
            throw new DbException("Server error while getting subscription", e);
        }
    }

    /**
     * Finds subscription by exact name match.
     *
     * @param name unique subscription name
     * @return matching subscription
     * @throws DbException if query execution fails
     */
    public Subscription get(String name) {
        try {
            Optional<Subscription> subscriptionOpt = subscriptionRepository.findByName(name);
            if (subscriptionOpt.isEmpty()) {
                throw new NotFoundException("Subscription not found");
            }

            return subscriptionOpt.orElseThrow();
        } catch (Exception e) {
            throw new DbException("Server error while getting subscription", e);
        }
    }

    /**
     * Calculates top most-used subscriptions with usage counts.
     *
     * @param limit maximum number of results to return
     * @return list of subscription usage statistics ordered by popularity
     * @throws DbException if statistics aggregation fails
     */
    public List<SubscriptionUsage> getTopPopular(int limit) {
        try {
            List<Object[]> result = subscriptionRepository.findTopPopularSubscriptions(limit);
            return result.stream().map(SubscriptionUsage::from).toList();
        } catch (Exception e) {
            throw new DbException("Server error while getting subscription usage", e);
        }
    }

    /**
     * Returns paginated subscriptions matching filter criteria.
     *
     * @param filter search parameters filtering criteria {@link SubscriptionFilter}
     * @param page zero-based page index
     * @param size number of items per page
     * @return page of subscriptions matching the filter
     * @throws DbException if data retrieval fails
     */
    public Page<Subscription> getAll(SubscriptionFilter filter, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Specification<Subscription> specification = SubscriptionSpecification
                    .filterSubscriptions(filter);
            return subscriptionRepository.findAll(specification, pageable);
        } catch (Exception e) {
            throw new DbException("Server error while getting subscriptions", e);
        }
    }

    /**
     * Updates subscription properties.
     *
     * @param editSubscription container with update data {@link ru.subscription_manager.service.entity.edit.EditSubscription}
     * @return modified subscription entity
     * @throws DbException if update operation fails
     */
    public Subscription edit(EditEntity<Subscription, UUID> editSubscription) {
        try {
            Optional<Subscription> subscriptionOpt = subscriptionRepository
                    .findById(editSubscription.id());
            if (subscriptionOpt.isEmpty()) {
                throw new NotFoundException("Subscription for edit not found");
            }

            Subscription subscription = editSubscription.edit(subscriptionOpt.orElseThrow());

            return subscriptionRepository.save(subscription);
        } catch (Exception e) {
            throw new DbException("Server error while editing subscription", e);
        }
    }

    /**
     * Permanently removes subscription.
     *
     * @param id UUID of subscription to delete
     * @throws DbException if deletion operation fails
     */
    public void delete(UUID id) {
        try {
            Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(id);
            if (subscriptionOpt.isEmpty()) {
                throw new NotFoundException("Subscription for delete not found");
            }
            subscriptionRepository.deleteById(id);
        } catch (Exception e) {
            throw new DbException("Server error while deleting subscription", e);
        }
    }

}
