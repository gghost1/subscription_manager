package ru.subscription_manager.data.subscription;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.subscription_manager.data.user.User;
import ru.subscription_manager.data.users_subscription.UsersSubscription;
import ru.subscription_manager.service.entity.filter.SubscriptionFilter;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionSpecification {

    public static Specification<Subscription> filterSubscriptions(SubscriptionFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            filter.user().ifPresent(userId -> {
                Join<Subscription, UsersSubscription> userSubscriptionJoin = root.join("userSubscriptions");
                Predicate hasSubscription = criteriaBuilder.equal(
                        userSubscriptionJoin.get("user").get("id"),
                        userId
                );
                Predicate isActive = criteriaBuilder.isTrue(userSubscriptionJoin.get("active"));
                predicates.add(criteriaBuilder.and(hasSubscription, isActive));
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
