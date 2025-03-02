package ru.subscription_manager.data.subscription;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    String name;

    @Column(name = "createdAt", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDate updatedAt;

    public Subscription(String name, LocalDate createdAt) {
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = LocalDate.now();
    }
}
