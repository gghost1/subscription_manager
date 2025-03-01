package ru.subscription_manager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.subscription_manager.controller.entity.request.subscription.EditSubscriptionRequestDto;
import ru.subscription_manager.controller.entity.request.subscription.SubscriptionFilterRequestDto;
import ru.subscription_manager.controller.entity.response.PaginatedList;
import ru.subscription_manager.controller.entity.response.SubscriptionResponseDto;
import ru.subscription_manager.controller.entity.response.SubscriptionUsageResponseDto;
import ru.subscription_manager.data.subscription.Subscription;
import ru.subscription_manager.service.SubscriptionService;
import ru.subscription_manager.service.entity.edit.EditSubscription;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDto> getSubscription(@PathVariable UUID id) {
        return ResponseEntity.ok().body(SubscriptionResponseDto.fromSubscription(subscriptionService.get(id)));
    }

    @GetMapping("/by-name")
    public ResponseEntity<SubscriptionResponseDto> getSubscriptionByName(@RequestParam String name) {
        return ResponseEntity.ok().body(SubscriptionResponseDto.fromSubscription(subscriptionService.get(name)));
    }

    @GetMapping("/top")
    public ResponseEntity<List<SubscriptionUsageResponseDto>> getTopSubscription(@RequestParam int count) {
        return ResponseEntity.ok().body(subscriptionService.getTopPopular(count).stream().map(SubscriptionUsageResponseDto::fromSubscriptionUsage).toList());
    }

    @GetMapping
    public ResponseEntity<PaginatedList<SubscriptionResponseDto>> getSubscriptions(@Valid SubscriptionFilterRequestDto subscriptionFilterRequestDto, int page, int size) {
        Page<Subscription> subscriptions = subscriptionService.getAll(subscriptionFilterRequestDto.toSubscriptionFilter(), page, size);
        return ResponseEntity.ok(new PaginatedList<>(
                page,
                size,
                subscriptions.getTotalElements(),
                subscriptions.getContent().stream().map(SubscriptionResponseDto::fromSubscription).toList())
        );
    }

    @PutMapping
    public ResponseEntity<SubscriptionResponseDto> updateSubscription(@Valid @RequestBody EditSubscriptionRequestDto editSubscriptionRequestDto) {
        EditSubscription editSubscription = editSubscriptionRequestDto.toEditSubscription();
        return ResponseEntity.ok().body(SubscriptionResponseDto.fromSubscription(subscriptionService.edit(editSubscription)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable UUID id) {
        subscriptionService.delete(id);
        return ResponseEntity.ok().build();
    }

}
