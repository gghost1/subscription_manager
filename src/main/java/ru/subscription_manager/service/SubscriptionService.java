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
import ru.subscription_manager.data.user.User;
import ru.subscription_manager.service.entity.create.CreateEntity;
import ru.subscription_manager.service.entity.edit.EditEntity;
import ru.subscription_manager.service.entity.filter.SubscriptionFilter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public Subscription add(CreateEntity<Subscription> createSubscription) {
        try {
            Subscription subscription = createSubscription.create();

            Optional<Subscription> subscriptionOpt = subscriptionRepository.findByName(subscription.getName());
            if (subscriptionOpt.isPresent()) {
                return subscriptionOpt.orElseThrow();
            }

            return subscriptionRepository.save(subscription);
        } catch (Exception e) {
            throw new DbException("Server error while adding subscription", e);
        }
    }

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

    public Page<Subscription> getAll(SubscriptionFilter filter, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Specification<Subscription> specification = SubscriptionSpecification.filterSubscriptions(filter);
            return subscriptionRepository.findAll(specification, pageable);
        } catch (Exception e) {
            throw new DbException("Server error while getting subscriptions", e);
        }
    }

    public Subscription edit(EditEntity<Subscription, UUID> editSubscription) {
        try {
            Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(editSubscription.id());
            if (subscriptionOpt.isEmpty()) {
                throw new NotFoundException("Subscription for edit not found");
            }

            Subscription subscription = editSubscription.edit(subscriptionOpt.orElseThrow());

            return subscriptionRepository.save(subscription);
        } catch (Exception e) {
            throw new DbException("Server error while editing subscription", e);
        }
    }

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
