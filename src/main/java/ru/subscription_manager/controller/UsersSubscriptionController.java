package ru.subscription_manager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.subscription_manager.controller.entity.request.subscription.CreateSubscriptionRequestDto;
import ru.subscription_manager.controller.entity.request.users_subscription.EditUsersSubscriptionRequestDto;
import ru.subscription_manager.controller.entity.request.users_subscription.UsersSubscriptionFilterRequestDto;
import ru.subscription_manager.controller.entity.response.PaginatedList;
import ru.subscription_manager.controller.entity.response.UsersSubscriptionResponseDto;
import ru.subscription_manager.data.subscription.Subscription;
import ru.subscription_manager.data.users_subscription.UserSubscriptionId;
import ru.subscription_manager.data.users_subscription.UsersSubscription;
import ru.subscription_manager.service.SubscriptionService;
import ru.subscription_manager.service.UsersSubscriptionService;
import ru.subscription_manager.service.entity.create.CreateSubscription;
import ru.subscription_manager.service.entity.create.CreateUsersSubscription;
import ru.subscription_manager.service.entity.edit.EditUsersSubscription;
import ru.subscription_manager.service.entity.filter.UsersSubscriptionFilter;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/users/{id}/subscriptions")
@RequiredArgsConstructor
public class UsersSubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UsersSubscriptionService usersSubscriptionService;

    @PostMapping
    public ResponseEntity<UsersSubscriptionResponseDto> addSubscription(@PathVariable UUID id, @Valid @RequestBody CreateSubscriptionRequestDto createSubscriptionRequestDto) {
        CreateSubscription createSubscription = createSubscriptionRequestDto.toCreateSubscription();

        Subscription subscription = subscriptionService.add(createSubscription);
        UsersSubscription usersSubscription = usersSubscriptionService.add(new CreateUsersSubscription(
                id,
                subscription.getId(),
                LocalDate.now().plusDays(createSubscriptionRequestDto.period()),
                true
        ));
        return ResponseEntity.ok(UsersSubscriptionResponseDto.fromUsersSubscription(usersSubscription));
    }

    @GetMapping
    public ResponseEntity<PaginatedList<UsersSubscriptionResponseDto>> getSubscription(@PathVariable UUID id, @Valid UsersSubscriptionFilterRequestDto usersSubscriptionFilterRequestDto, int page, int size) {
        UsersSubscriptionFilter usersSubscriptionFilter = usersSubscriptionFilterRequestDto.toUsersSubscriptionFilter();

        Page<UsersSubscription> usersSubscriptions = usersSubscriptionService.getAll(usersSubscriptionFilter, id, page, size);
        return ResponseEntity.ok(new PaginatedList<>(
                page,
                size,
                usersSubscriptions.getTotalElements(),
                usersSubscriptions.getContent().stream().map(UsersSubscriptionResponseDto::fromUsersSubscription).toList()
        ));
    }

    @PutMapping("/{subscriptionId}")
    public ResponseEntity<UsersSubscriptionResponseDto> editSubscription(@PathVariable UUID id, @PathVariable UUID subscriptionId, @Valid @RequestBody EditUsersSubscriptionRequestDto editUsersSubscriptionRequestDto) {
        EditUsersSubscription editUsersSubscription = editUsersSubscriptionRequestDto.toEditUsersSubscription(new UserSubscriptionId(id, subscriptionId));

        UsersSubscription usersSubscription = usersSubscriptionService.edit(editUsersSubscription);
        return ResponseEntity.ok(UsersSubscriptionResponseDto.fromUsersSubscription(usersSubscription));
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable UUID id, @PathVariable UUID subscriptionId) {
        usersSubscriptionService.delete(new UserSubscriptionId(id, subscriptionId));
        return ResponseEntity.ok().build();
    }

}
