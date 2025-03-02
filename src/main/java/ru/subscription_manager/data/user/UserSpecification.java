package ru.subscription_manager.data.user;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.subscription_manager.data.users_subscription.UsersSubscription;
import ru.subscription_manager.service.entity.filter.UserFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to filter users entities based on specified criteria
 */
public class UserSpecification {

    /**
     * Creates a specification for filtering user entities
     *
     * @param filter filter criteria {@link UserFilter}
     * @return specification for users
     */
    public static Specification<User> filterUsers(UserFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            filter.firstName().ifPresent(firstName ->
                    predicates.add(criteriaBuilder.equal(root.get("firstName"), firstName))
            );

            filter.secondName().ifPresent(secondName ->
                    predicates.add(criteriaBuilder.equal(root.get("secondName"), secondName))
            );

            filter.subscriptionId().ifPresent(subscriptionId -> {
                Join<User, UsersSubscription> userSubscriptionJoin = root.join("userSubscriptions");
                Predicate hasSubscription = criteriaBuilder.equal(
                        userSubscriptionJoin.get("subscriptionId").get("id"),
                        subscriptionId
                );
                Predicate isActive = criteriaBuilder.isTrue(userSubscriptionJoin.get("active"));
                predicates.add(criteriaBuilder.and(hasSubscription, isActive));
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
