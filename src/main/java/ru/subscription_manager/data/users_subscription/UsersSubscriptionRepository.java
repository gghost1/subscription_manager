package ru.subscription_manager.data.users_subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersSubscriptionRepository extends JpaRepository<UsersSubscription, UserSubscriptionId>, JpaSpecificationExecutor<UsersSubscription> {

}
