package ru.subscription_manager.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import ru.subscription_manager.configuration.JpaTestConfiguration;
import ru.subscription_manager.configuration.exception.DbException;
import ru.subscription_manager.data.type.email.Email;
import ru.subscription_manager.data.user.User;
import ru.subscription_manager.service.entity.create.CreateUser;
import ru.subscription_manager.service.entity.edit.EditUser;
import ru.subscription_manager.service.entity.filter.UserFilter;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
public class UserServiceTest extends JpaTestConfiguration {

    @Autowired
    private UserService userService;

    @Test
    void addAndGetTest() {
        String email = "example@ya.ru";
        CreateUser createUser = new CreateUser(
                "FirstName",
                "SecondName",
                null,
                email
        );

        User user = userService.add(createUser);

        assertEquals(createUser.firstName(), user.getName().getFirstName());
        assertEquals(createUser.secondName(), user.getName().getSecondName());
        assertEquals(createUser.lastName(), user.getName().getLastName());
        assertEquals(createUser.email(), user.getEmail().value());

        User userById = userService.get(user.getId());
        compareUsers(user, userById);

        User userByEmail = userService.get(new Email(email));
        compareUsers(user, userByEmail);

        assertThrows(DataIntegrityViolationException.class, () -> userService.add(
                new CreateUser(
                        "FirstName1",
                        "SecondName",
                        null,
                        "example@ya.ru"
                )
        ));

        assertThrows(InvalidDataAccessApiUsageException.class, () -> userService.add(
                new CreateUser(
                        "FirstName",
                        "SecondName",
                        null,
                        "exampleya.ru"
                )
        ));


        userService.delete(user.getId());
    }

    @Test
    void editTest() {
        String email = "example1@ya.ru";
        CreateUser createUser = new CreateUser(
                "FirstName",
                "SecondName",
                null,
                email
        );

        User user = userService.add(createUser);

        String newEmail1 = "example2@ya.ru";
        EditUser editUser1 = new EditUser(
                user.getId(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(newEmail1)
        );

        User editedUser1 = userService.edit(editUser1);
        compareUsers(editUser1.edit(user), editedUser1);
        compareUsers(editedUser1, userService.get(new Email(newEmail1)));

        String newEmail2 = "example3@ya.ru";
        EditUser editUser2 = new EditUser(
                user.getId(),
                Optional.of("EditedFirstName"),
                Optional.of("EditedSecondName"),
                Optional.of("LastName"),
                Optional.of(newEmail2)
        );

        User editedUser2 = userService.edit(editUser2);
        compareUsers(editUser2.edit(user), editedUser2);
        compareUsers(editedUser2, userService.get(new Email(newEmail2)));

        userService.delete(user.getId());

        System.out.println(userService.getAll(new UserFilter(
                Optional.empty(),
                Optional.empty()
        ), 0, 1).getTotalElements());
    }

    @Test
    void deleteTest() {
        String email = "example@ya.ru";
        CreateUser createUser = new CreateUser(
                "FirstName",
                "SecondName",
                null,
                email
        );

        User user = userService.add(createUser);

        userService.delete(user.getId());
        assertThrows(DbException.class, () -> userService.get(user.getId()));
    }

    @Test
    void getAllWithoutFilterTest() {
        String email1 = "1example@ya.ru";
        String email2 = "2example@ya.ru";
        User user1 = userService.add(new CreateUser("a", "a", null, email1));
        User user2 = userService.add(new CreateUser("b", "b", null, email2));

        Page<User> users1 = userService.getAll(new UserFilter(
                Optional.empty(),
                Optional.empty()
        ), 0, 1);

        List<User> usersList = users1.getContent();
        boolean first1 = false;
        if (user1.getId().equals(usersList.get(0).getId())) {
            first1 = true;
            compareUsers(user1, usersList.get(0));
        } else if(user2.getId().equals(usersList.get(0).getId())) {
            compareUsers(user2, usersList.get(0));
        } else {
            fail();
        }

        Page<User> users2 = userService.getAll(new UserFilter(
                Optional.empty(),
                Optional.empty()
        ), 1, 1);

        usersList = users2.getContent();
        if (first1) {
            compareUsers(user2, usersList.get(0));
        } else {
            compareUsers(user1, usersList.get(0));
        }

        userService.delete(user1.getId());
        userService.delete(user2.getId());
    }

    @Test
    void getAllWithFilterTest() {
        String email1 = "1example@ya.ru";
        String email2 = "2example@ya.ru";
        User user1 = userService.add(new CreateUser("ab", "a", null, email1));
        User user2 = userService.add(new CreateUser("b", "b", null, email2));

        Page<User> users1 = userService.getAll(new UserFilter(
                Optional.of("a"),
                Optional.empty()
        ), 0, 1);

        List<User> usersList = users1.getContent();
        compareUsers(user1, usersList.get(0));

        Page<User> users2 = userService.getAll(new UserFilter(
                Optional.of("a"),
                Optional.empty()
        ), 1, 1);

        assertTrue(users2.getContent().isEmpty());

        Page<User> users3 = userService.getAll(new UserFilter(
                Optional.empty(),
                Optional.of("b")
        ), 0, 1);

        usersList = users3.getContent();
        compareUsers(user2, usersList.get(0));


        Page<User> users4 = userService.getAll(new UserFilter(
                Optional.of("a"),
                Optional.of("b")
        ), 0, 1);

        assertTrue(users4.getContent().isEmpty());

        Page<User> users5 = userService.getAll(new UserFilter(
                Optional.of("ab"),
                Optional.empty()
        ), 0, 1);

        compareUsers(user1, users5.getContent().get(0));

        userService.delete(user1.getId());
        userService.delete(user2.getId());
    }

    private void compareUsers(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getName().getFirstName(), user2.getName().getFirstName());
        assertEquals(user1.getName().getSecondName(), user2.getName().getSecondName());
        assertEquals(user1.getName().getLastName(), user2.getName().getLastName());
        assertEquals(user1.getEmail().value(), user2.getEmail().value());
    }

}
