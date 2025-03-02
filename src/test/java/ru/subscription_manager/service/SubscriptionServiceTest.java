package ru.subscription_manager.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import ru.subscription_manager.configuration.JpaTestConfiguration;
import ru.subscription_manager.configuration.exception.DbException;
import ru.subscription_manager.data.subscription.Subscription;
import ru.subscription_manager.data.subscription.SubscriptionUsage;
import ru.subscription_manager.data.user.User;
import ru.subscription_manager.service.entity.create.CreateSubscription;
import ru.subscription_manager.service.entity.create.CreateUser;
import ru.subscription_manager.service.entity.create.CreateUsersSubscription;
import ru.subscription_manager.service.entity.edit.EditSubscription;
import ru.subscription_manager.service.entity.filter.SubscriptionFilter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SubscriptionServiceTest extends JpaTestConfiguration {

    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private UserService userService;
    @Autowired
    private UsersSubscriptionService usersSubscriptionService;

    @Test
    void addAndGetTest() {
        String subName = "TestSubscription";
        CreateSubscription createSubscription = new CreateSubscription(subName);
        Subscription subscription = subscriptionService.add(createSubscription);

        assertEquals(subName, subscription.getName());

        Subscription subscriptionById = subscriptionService.get(subscription.getId());
        compareSubscriptions(subscription, subscriptionById);

        Subscription subscriptionByName = subscriptionService.get(subName);
        compareSubscriptions(subscription, subscriptionByName);

        subscriptionService.delete(subscription.getId());
    }

    @Test
    void editTest() {
        String subName = "TestSubscription";
        CreateSubscription createSubscription = new CreateSubscription(subName);
        Subscription subscription = subscriptionService.add(createSubscription);

        String newSubName = "EditedSubscription";
        EditSubscription editSubscription = new EditSubscription(
                subscription.getId(),
                Optional.of(newSubName)
        );

        Subscription editedSubscription = subscriptionService.edit(editSubscription);
        compareSubscriptions(editSubscription.edit(subscription), editedSubscription);
        compareSubscriptions(editedSubscription, subscriptionService.get(newSubName));

        subscriptionService.delete(subscription.getId());
    }

    @Test
    void deleteTest() {
        String subName = "TestSubscription";
        CreateSubscription createSubscription = new CreateSubscription(subName);
        Subscription subscription = subscriptionService.add(createSubscription);

        subscriptionService.delete(subscription.getId());
        assertThrows(DbException.class, () -> subscriptionService.get(subscription.getId()));
    }

    @Test
    void getAllWithoutFilterTest() {
        String subName1 = "Sub1";
        String subName2 = "Sub2";
        Subscription subscription1 = subscriptionService.add(new CreateSubscription(subName1));
        Subscription subscription2 = subscriptionService.add(new CreateSubscription(subName2));

        Page<Subscription> page1 = subscriptionService.getAll(new SubscriptionFilter(Optional.empty()), 0, 1);
        List<Subscription> list1 = page1.getContent();
        boolean firstIsSub1 = false;
        if (subscription1.getId().equals(list1.get(0).getId())) {
            firstIsSub1 = true;
            compareSubscriptions(subscription1, list1.get(0));
        } else if (subscription2.getId().equals(list1.get(0).getId())) {
            compareSubscriptions(subscription2, list1.get(0));
        } else {
            fail();
        }

        Page<Subscription> page2 = subscriptionService.getAll(new SubscriptionFilter(Optional.empty()), 1, 1);
        List<Subscription> list2 = page2.getContent();
        if (firstIsSub1) {
            compareSubscriptions(subscription2, list2.get(0));
        } else {
            compareSubscriptions(subscription1, list2.get(0));
        }

        subscriptionService.delete(subscription1.getId());
        subscriptionService.delete(subscription2.getId());
    }

    @Test
    void getAllWithFilterTest() {
        String subName1 = "FilterSub1";
        String subName2 = "FilterSub2";
        Subscription subscription1 = subscriptionService.add(new CreateSubscription(subName1));
        Subscription subscription2 = subscriptionService.add(new CreateSubscription(subName2));

        Page<Subscription> page1 = subscriptionService.getAll(new SubscriptionFilter(Optional.of(subName1)), 0, 1);
        List<Subscription> list1 = page1.getContent();
        compareSubscriptions(subscription1, list1.get(0));

        Page<Subscription> page2 = subscriptionService.getAll(new SubscriptionFilter(Optional.of(subName1)), 1, 1);
        assertTrue(page2.getContent().isEmpty());

        Page<Subscription> page3 = subscriptionService.getAll(new SubscriptionFilter(Optional.of("NonExistent")), 0, 1);
        assertTrue(page3.getContent().isEmpty());

        Page<Subscription> page4 = subscriptionService.getAll(new SubscriptionFilter(Optional.of(subName1)), 0, 2);
        assertTrue(
                Stream.of(
                        subscription1,
                        subscription2
                )
                        .map(Subscription::getId)
                        .toList()
                        .containsAll(page4.getContent().stream().map(Subscription::getId).toList())
        );

        subscriptionService.delete(subscription1.getId());
        subscriptionService.delete(subscription2.getId());
    }

    @Test
    void getTop() {
        User user1 = userService.add(new CreateUser("a", "a", null, "a@ya.ru"));
        User user2 = userService.add(new CreateUser("b", "b", null, "b@ya.ru"));
        Subscription subscription1 = subscriptionService.add(new CreateSubscription("Sub1"));
        Subscription subscription2 = subscriptionService.add(new CreateSubscription("Sub2"));
        Subscription subscription3 = subscriptionService.add(new CreateSubscription("Sub3"));

        usersSubscriptionService.add(new CreateUsersSubscription(
                user1.getId(),
                subscription1.getId(),
                LocalDate.now(),
                true
        ));
        usersSubscriptionService.add(new CreateUsersSubscription(
                user2.getId(),
                subscription1.getId(),
                LocalDate.now(),
                true
        ));
        usersSubscriptionService.add(new CreateUsersSubscription(
                user1.getId(),
                subscription2.getId(),
                LocalDate.now(),
                true
        ));

        List<SubscriptionUsage> topSubscriptions = subscriptionService.getTopPopular(2);

        assertEquals(subscription1.getId(), topSubscriptions.get(0).subscriptionId());
        assertEquals(2, topSubscriptions.get(0).userCount());
        assertEquals(subscription2.getId(), topSubscriptions.get(1).subscriptionId());
        assertEquals(1, topSubscriptions.get(1).userCount());


        subscriptionService.delete(subscription1.getId());
        subscriptionService.delete(subscription2.getId());
        subscriptionService.delete(subscription3.getId());
    }

    private void compareSubscriptions(Subscription expected, Subscription actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
    }


}
