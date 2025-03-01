package ru.subscription_manager.data.users_subscription;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.subscription_manager.service.entity.filter.UsersSubscriptionFilter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsersSubscriptionSpecification {

    public static Specification<UsersSubscription> filterUsersSubscriptions(UsersSubscriptionFilter filter, UUID id) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("user").get("id"), id));

            filter.active().ifPresent(active ->
                    predicates.add(criteriaBuilder.equal(root.get("active"), active))
            );

            filter.expirationDate().ifPresent(expFilter -> {
                Path<LocalDate> expirationDatePath = root.get("expirationDate");
                switch (expFilter.comparison()) {
                    case EQUALS -> predicates.add(criteriaBuilder.equal(expirationDatePath, expFilter.expirationDate()));
                    case BEFORE -> predicates.add(criteriaBuilder.lessThan(expirationDatePath, expFilter.expirationDate()));
                    case AFTER -> predicates.add(criteriaBuilder.greaterThan(expirationDatePath, expFilter.expirationDate()));
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
