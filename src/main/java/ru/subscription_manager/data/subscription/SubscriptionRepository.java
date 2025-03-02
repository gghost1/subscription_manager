package ru.subscription_manager.data.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID>, JpaSpecificationExecutor<Subscription> {

    /**
     * Custom method to get most popular subscriptions
     * @param limit maximum number of results
     * @return row array of row data of subscription usage statistics
     */
    @Query(value = """
        SELECT s.id, s.name, COUNT(us.subscription_id) as user_count 
        FROM subscriptions s
        LEFT JOIN users_subscriptions us ON s.id = us.subscription_id
        GROUP BY s.id
        ORDER BY user_count DESC, s.name ASC
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> findTopPopularSubscriptions(@Param("limit") int limit);

    Optional<Subscription> findByName(String name);

}
