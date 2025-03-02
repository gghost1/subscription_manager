package ru.subscription_manager.data.user;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
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
                    predicates.add(criteriaBuilder.like(root.get("firstName"), firstName + "%"))
            );

            filter.secondName().ifPresent(secondName ->
                    predicates.add(criteriaBuilder.like(root.get("secondName"), secondName + "%"))
            );

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
