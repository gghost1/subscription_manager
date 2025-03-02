package ru.subscription_manager.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import ru.subscription_manager.controller.entity.request.subscription.CreateSubscriptionRequestDto;
import ru.subscription_manager.controller.entity.request.users_subscription.EditUsersSubscriptionRequestDto;
import ru.subscription_manager.controller.entity.request.users_subscription.UsersSubscriptionFilterRequestDto;
import ru.subscription_manager.controller.entity.response.ExceptionResponseDto;
import ru.subscription_manager.controller.entity.response.PaginatedList;
import ru.subscription_manager.controller.entity.response.UsersSubscriptionResponseDto;
import ru.subscription_manager.data.subscription.Subscription;
import ru.subscription_manager.data.users_subscription.UsersSubscription;
import ru.subscription_manager.data.users_subscription.UsersSubscriptionId;
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
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operation done successfully", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "Incorrect request",
                content = @io.swagger.v3.oas.annotations.media.Content(
                        schema = @io.swagger.v3.oas.annotations.media.Schema(
                                implementation = ExceptionResponseDto.class)
                )),
        @ApiResponse(responseCode = "404", description = "Not found",
                content = @io.swagger.v3.oas.annotations.media.Content(
                        schema = @io.swagger.v3.oas.annotations.media.Schema(
                                implementation = ExceptionResponseDto.class)
                )),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @io.swagger.v3.oas.annotations.media.Content(
                        schema = @io.swagger.v3.oas.annotations.media.Schema(
                                implementation = ExceptionResponseDto.class)
                ))
})
public class UsersSubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UsersSubscriptionService usersSubscriptionService;

    @PostMapping
    public ResponseEntity<UsersSubscriptionResponseDto> addSubscription(
            @PathVariable
            @Valid
            @NotNull(message = "Id is required")
            UUID id,
            @RequestBody
            @Valid
            @NotNull(message = "Create subscription entity is required")
            CreateSubscriptionRequestDto createSubscriptionRequestDto
    ) {
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
    public ResponseEntity<PaginatedList<UsersSubscriptionResponseDto>> getSubscription(
            @PathVariable
            @Valid
            @NotNull(message = "Id is required")
            UUID id,
            @Valid
            UsersSubscriptionFilterRequestDto usersSubscriptionFilterRequestDto,
            @Valid
            @NotNull(message = "Page is required")
            @Min(value = 0, message = "Page must be greater or equal to 0")
            int page,
            @Valid
            @NotNull(message = "Size is required")
            @Min(value = 1, message = "Size must be greater than 0")
            int size
    ) {
        UsersSubscriptionFilter usersSubscriptionFilter = usersSubscriptionFilterRequestDto
                .toUsersSubscriptionFilter();

        Page<UsersSubscription> usersSubscriptions = usersSubscriptionService
                .getAll(usersSubscriptionFilter, id, page, size);

        return ResponseEntity.ok(new PaginatedList<>(
                page,
                size,
                usersSubscriptions.getTotalElements(),
                usersSubscriptions.getContent().stream().map(UsersSubscriptionResponseDto::fromUsersSubscription).toList()
        ));
    }

    @PutMapping("/{subscriptionId}")
    public ResponseEntity<UsersSubscriptionResponseDto> editSubscription(
            @PathVariable
            @Valid
            @NotNull(message = "User id is required")
            UUID id,
            @PathVariable
            @Valid
            @NotNull(message = "Subscription id is required")
            UUID subscriptionId,
            @RequestBody
            @Valid
            EditUsersSubscriptionRequestDto editUsersSubscriptionRequestDto
    ) {
        EditUsersSubscription editUsersSubscription = editUsersSubscriptionRequestDto
                .toEditUsersSubscription(new UsersSubscriptionId(id, subscriptionId));

        UsersSubscription usersSubscription = usersSubscriptionService.edit(editUsersSubscription);
        return ResponseEntity.ok(
                UsersSubscriptionResponseDto.fromUsersSubscription(usersSubscription));
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<Void> deleteSubscription(
            @PathVariable
            @Valid
            @NotNull(message = "User id is required")
            UUID id,
            @PathVariable
            @Valid
            @NotNull(message = "Subscription id is required")
            UUID subscriptionId
    ) {
        usersSubscriptionService.delete(new UsersSubscriptionId(id, subscriptionId));
        return ResponseEntity.ok().build();
    }

}
