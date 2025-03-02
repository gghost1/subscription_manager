package ru.subscription_manager.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.subscription_manager.controller.entity.request.user.CreateUserRequestDto;
import ru.subscription_manager.controller.entity.request.user.EditUserRequestDto;
import ru.subscription_manager.controller.entity.request.user.UserFilterRequestDto;
import ru.subscription_manager.controller.entity.response.PaginatedList;
import ru.subscription_manager.controller.entity.response.UserResponseDto;
import ru.subscription_manager.data.type.email.Email;
import ru.subscription_manager.data.user.User;
import ru.subscription_manager.service.UserService;
import ru.subscription_manager.service.entity.create.CreateUser;
import ru.subscription_manager.service.entity.edit.EditUser;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @RequestBody
            @Valid
            @NotNull(message = "Create user entity is required")
            CreateUserRequestDto createUserRequestDto
    ) {
        CreateUser createUser = createUserRequestDto.toCreateUser();

        return ResponseEntity.ok(
                UserResponseDto.fromUser(userService.add(createUser)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(
            @PathVariable
            @Valid
            @NotNull(message = "Id is required")
            UUID id
    ) {
        return ResponseEntity.ok(
                UserResponseDto.fromUser(userService.get(id)));
    }

    @GetMapping("/by-email")
    public ResponseEntity<UserResponseDto> getUserByEmail(
            @RequestParam
            @Valid
            @NotNull(message = "Email is required")
            @NotBlank(message = "Email is required")
            String email
    ) {
        return ResponseEntity.ok(
                UserResponseDto.fromUser(userService.get(new Email(email))));
    }

    @GetMapping("/all")
    public ResponseEntity<PaginatedList<UserResponseDto>> getAll(
            @Valid
            UserFilterRequestDto userFilterRequestDto,
            @Valid
            @NotNull(message = "Page is required")
            @Min(value = 0, message = "Page must be greater or equal to 0")
            int page,
            @Valid
            @NotNull(message = "Size is required")
            @Min(value = 1, message = "Size must be greater than 0")
            int size
    ) {
        Page<User> users = userService.getAll(userFilterRequestDto.toUserFilter(), page, size);

        return ResponseEntity.ok(new PaginatedList<>(
                page,
                size,
                users.getTotalElements(),
                users.getContent().stream().map(UserResponseDto::fromUser).toList()
        ));
    }

    @PutMapping
    public ResponseEntity<UserResponseDto> editUser(
            @RequestBody
            @Valid
            EditUserRequestDto editUserRequestDto
    ) {
        EditUser editUser = editUserRequestDto.toEditUser();

        return ResponseEntity.ok(UserResponseDto.fromUser(userService.edit(editUser)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable
            @Valid
            @NotNull(message = "Id is required")
            UUID id
    ) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

}