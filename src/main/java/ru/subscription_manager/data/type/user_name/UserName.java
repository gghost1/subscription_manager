package ru.subscription_manager.data.type.user_name;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserName {

    @NotBlank
    @NotNull
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$")
    private String firstName;
    @NotBlank
    @NotNull
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$")
    private String secondName;
    @Nullable
    private String lastName;

}
