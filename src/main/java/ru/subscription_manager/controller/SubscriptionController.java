package ru.subscription_manager.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.subscription_manager.controller.entity.request.subscription.EditSubscriptionRequestDto;
import ru.subscription_manager.controller.entity.request.subscription.SubscriptionFilterRequestDto;
import ru.subscription_manager.controller.entity.response.ExceptionResponseDto;
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
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDto> getSubscription(
            @PathVariable
            @Valid
            @NotNull(message = "Id is required")
            UUID id
    ) {
        return ResponseEntity.ok().body(
                SubscriptionResponseDto.fromSubscription(subscriptionService.get(id)));
    }

    @GetMapping("/by-name")
    public ResponseEntity<SubscriptionResponseDto> getSubscriptionByName(
            @RequestParam
            @Valid
            @NotNull(message = "Name is required")
            @NotBlank(message = "Name is required")
            String name
    ) {
        return ResponseEntity.ok().body(
                SubscriptionResponseDto.fromSubscription(subscriptionService.get(name)));
    }

    @GetMapping("/top")
    public ResponseEntity<List<SubscriptionUsageResponseDto>> getTopSubscription(
            @RequestParam
            @Valid
            @NotNull(message = "Count is required")
            @Min(value = 1, message = "Count must be greater than 0")
            Integer count
    ) {
        return ResponseEntity.ok().body(
                subscriptionService.getTopPopular(count).stream()
                        .map(SubscriptionUsageResponseDto::fromSubscriptionUsage).toList());
    }

    @GetMapping
    public ResponseEntity<PaginatedList<SubscriptionResponseDto>> getSubscriptions(
            @Valid
            SubscriptionFilterRequestDto subscriptionFilterRequestDto,
            @Valid
            @NotNull(message = "Page is required")
            @Min(value = 0, message = "Page must be greater or equal to 0")
            Integer page,
            @Valid
            @NotNull(message = "Size is required")
            @Min(value = 1, message = "Size must be greater than 0")
            Integer size
    ) {
        Page<Subscription> subscriptions = subscriptionService
                .getAll(subscriptionFilterRequestDto.toSubscriptionFilter(), page, size);
        return ResponseEntity.ok(new PaginatedList<>(
                page,
                size,
                subscriptions.getTotalElements(),
                subscriptions.getContent().stream().map(SubscriptionResponseDto::fromSubscription).toList())
        );
    }

    @PutMapping
    public ResponseEntity<SubscriptionResponseDto> updateSubscription(
            @RequestBody
            @Valid
            @NotNull(message = "Edit subscription entity is required")
            EditSubscriptionRequestDto editSubscriptionRequestDto
    ) {
        EditSubscription editSubscription = editSubscriptionRequestDto.toEditSubscription();
        return ResponseEntity.ok().body(
                SubscriptionResponseDto.fromSubscription(subscriptionService.edit(editSubscription)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(
            @PathVariable
            @Valid
            @NotNull(message = "Id is required")
            UUID id
    ) {
        subscriptionService.delete(id);
        return ResponseEntity.ok().build();
    }

}
