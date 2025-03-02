package ru.subscription_manager.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.subscription_manager.configuration.JpaTestConfiguration;
import ru.subscription_manager.data.subscription.Subscription;
import ru.subscription_manager.data.user.User;
import ru.subscription_manager.data.users_subscription.UsersSubscription;
import ru.subscription_manager.service.entity.create.CreateSubscription;
import ru.subscription_manager.service.entity.create.CreateUser;
import ru.subscription_manager.service.entity.create.CreateUsersSubscription;
import ru.subscription_manager.service.entity.edit.EditUsersSubscription;
import ru.subscription_manager.service.entity.filter.ComparisonType;
import ru.subscription_manager.service.entity.filter.ExpirationDateFilter;
import ru.subscription_manager.service.entity.filter.UsersSubscriptionFilter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersSubscriptionServiceTest extends JpaTestConfiguration {

    @Autowired
    private UsersSubscriptionService usersSubscriptionService;
    @Autowired
    private UserService userService;
    @Autowired
    private SubscriptionService subscriptionService;

    @Test
    void addTest() {
        User user = userService.add(new CreateUser("a", "a", null, "a@ya.ru"));
        Subscription subscription = subscriptionService.add(new CreateSubscription("Sub1"));

        LocalDate localDate = LocalDate.now();
        CreateUsersSubscription createUsersSubscription = new CreateUsersSubscription(
                user.getId(),
                subscription.getId(),
                localDate,
                true
        );

        UsersSubscription usersSubscription = usersSubscriptionService.add(createUsersSubscription);

        assertEquals(localDate, usersSubscription.getExpirationDate());
        assertEquals(user.getId(), usersSubscription.getId().userId());
        assertEquals(subscription.getId(), usersSubscription.getId().subscriptionId());

        usersSubscriptionService.delete(usersSubscription.getId());
        subscriptionService.delete(subscription.getId());
        userService.delete(user.getId());
    }

    @Test
    void getAllWithoutFilterTest() {
        User user = userService.add(new CreateUser("a", "a", null, "a@ya.ru"));
        Subscription subscription1 = subscriptionService.add(new CreateSubscription("Sub1"));
        Subscription subscription2 = subscriptionService.add(new CreateSubscription("Sub2"));

        LocalDate localDate = LocalDate.now();
        CreateUsersSubscription createUsersSubscription1 = new CreateUsersSubscription(
                user.getId(),
                subscription1.getId(),
                localDate,
                true
        );

        UsersSubscription usersSubscription1 = usersSubscriptionService.add(createUsersSubscription1);

        List<UsersSubscription> usersSubscriptions = usersSubscriptionService.getAll(new UsersSubscriptionFilter(
                Optional.empty(),
                Optional.empty()
        ), user.getId(), 0, 1).getContent();

        compareUsersSubscription(usersSubscription1, usersSubscriptions.get(0));

        CreateUsersSubscription createUsersSubscription2 = new CreateUsersSubscription(
                user.getId(),
                subscription2.getId(),
                localDate,
                true
        );

        UsersSubscription usersSubscription2 = usersSubscriptionService.add(createUsersSubscription2);

        usersSubscriptions = usersSubscriptionService.getAll(new UsersSubscriptionFilter(
                Optional.empty(),
                Optional.empty()
        ), user.getId(), 1, 1).getContent();

        if (usersSubscriptions.get(0).getId().subscriptionId().equals(subscription1.getId())) {
            compareUsersSubscription(usersSubscription1, usersSubscriptions.get(0));
        } else {
            compareUsersSubscription(usersSubscription2, usersSubscriptions.get(0));
        }

        userService.delete(user.getId());
        subscriptionService.delete(subscription1.getId());
        subscriptionService.delete(subscription2.getId());
    }

    @Test
    void getAllWithFilterTest() {
        User user = userService.add(new CreateUser("a", "a", null, "a@ya.ru"));
        Subscription subscription1 = subscriptionService.add(new CreateSubscription("Sub1"));
        Subscription subscription2 = subscriptionService.add(new CreateSubscription("Sub2"));

        LocalDate localDate = LocalDate.now();
        CreateUsersSubscription createUsersSubscription1 = new CreateUsersSubscription(
                user.getId(),
                subscription1.getId(),
                localDate,
                true
        );

        usersSubscriptionService.add(createUsersSubscription1);

        assertEquals(1, usersSubscriptionService.getAll(new UsersSubscriptionFilter(
                Optional.of(new ExpirationDateFilter(localDate, ComparisonType.EQUALS)),
                Optional.empty()
        ), user.getId(), 0, 1).getContent().size());

        assertEquals(0, usersSubscriptionService.getAll(new UsersSubscriptionFilter(
                Optional.of(new ExpirationDateFilter(localDate, ComparisonType.AFTER)),
                Optional.empty()
        ), user.getId(), 0, 1).getContent().size());

        assertEquals(0, usersSubscriptionService.getAll(new UsersSubscriptionFilter(
                Optional.of(new ExpirationDateFilter(localDate, ComparisonType.BEFORE)),
                Optional.empty()
        ), user.getId(), 0, 1).getContent().size());

        assertEquals(0, usersSubscriptionService.getAll(new UsersSubscriptionFilter(
                Optional.empty(),
                Optional.of(false)
        ), user.getId(), 0, 1).getContent().size());

        assertEquals(1, usersSubscriptionService.getAll(new UsersSubscriptionFilter(
                Optional.of(new ExpirationDateFilter(localDate, ComparisonType.EQUALS)),
                Optional.of(true)
        ), user.getId(), 0, 1).getContent().size());

        userService.delete(user.getId());
        subscriptionService.delete(subscription1.getId());
        subscriptionService.delete(subscription2.getId());
    }

    @Test
    void editTest() {
        User user = userService.add(new CreateUser("a", "a", null, "a@ya.ru"));
        Subscription subscription = subscriptionService.add(new CreateSubscription("Sub1"));

        LocalDate localDate = LocalDate.now();
        CreateUsersSubscription createUsersSubscription = new CreateUsersSubscription(
                user.getId(),
                subscription.getId(),
                localDate,
                true
        );
        UsersSubscription usersSubscription = usersSubscriptionService.add(createUsersSubscription);

        EditUsersSubscription editUsersSubscription = new EditUsersSubscription(
                usersSubscription.getId(),
                Optional.of(localDate.plusDays(1)),
                Optional.of(false)
        );
        UsersSubscription usersSubscriptionEdited = usersSubscriptionService.edit(editUsersSubscription);
        compareUsersSubscription(usersSubscriptionEdited, editUsersSubscription.edit(usersSubscription));

        compareUsersSubscription(
                usersSubscriptionService.getAll(
                        new UsersSubscriptionFilter(
                                Optional.empty(),
                                Optional.empty()
                        ), user.getId(), 0, 1).getContent().get(0),
                usersSubscriptionEdited
        );

        userService.delete(user.getId());
        subscriptionService.delete(subscription.getId());
    }

    @Test
    void deleteTest() {
        User user = userService.add(new CreateUser("a", "a", null, "a@ya.ru"));
        Subscription subscription = subscriptionService.add(new CreateSubscription("Sub1"));
        LocalDate localDate = LocalDate.now();
        CreateUsersSubscription createUsersSubscription = new CreateUsersSubscription(
                user.getId(),
                subscription.getId(),
                localDate,
                true
        );
        UsersSubscription usersSubscription = usersSubscriptionService.add(createUsersSubscription);
        usersSubscriptionService.delete(usersSubscription.getId());
        assertEquals(0, usersSubscriptionService.getAll(
                new UsersSubscriptionFilter(
                        Optional.empty(),
                        Optional.empty()
                ), user.getId(), 0, 1).getContent().size());

        userService.delete(user.getId());
        subscriptionService.delete(subscription.getId());
    }


    private void compareUsersSubscription(UsersSubscription expected, UsersSubscription actual) {
        assertEquals(expected.getId().userId(), actual.getId().userId());
        assertEquals(expected.getId().subscriptionId(), actual.getId().subscriptionId());
        assertEquals(expected.getExpirationDate(), actual.getExpirationDate());
        assertEquals(expected.isActive(), actual.isActive());
    }


}
