package ru.subscription_manager.data.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Formula;
import ru.subscription_manager.data.type.email.Email;
import ru.subscription_manager.data.type.email.EmailConverter;
import ru.subscription_manager.data.type.user_name.UserName;
import ru.subscription_manager.data.type.user_name.UserNameConverter;
import ru.subscription_manager.data.users_subscription.UsersSubscription;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Convert(converter = UserNameConverter.class)
    @Column(name = "name", columnDefinition = "USER_NAME", nullable = false)
    @ColumnTransformer(write = "?::USER_NAME")
    private UserName name;

    @Column(
            nullable = false,
            unique = true,
            columnDefinition = "EMAIL"
    )
    @Convert(converter = EmailConverter.class)
    private Email email;

    @Formula("((name).first_name)")
    private String firstName;

    @Formula("((name).second_name)")
    private String secondName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UsersSubscription> userSubscriptions;

    public User(UserName name, Email email) {
        this.name = name;
        this.email = email;
        this.userSubscriptions = new HashSet<>();
    }
}


