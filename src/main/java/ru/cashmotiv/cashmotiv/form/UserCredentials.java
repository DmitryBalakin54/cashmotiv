package ru.cashmotiv.cashmotiv.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentials {
    @NotBlank(message = "{validation.loginoremail.notblank}")
    private String loginOrEmail;

    @NotBlank(message = "{validation.password.notblank}")
    @Size(min = 10, max = 25, message = "{validation.password.size}")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{10,25}$",
            message = "{validation.password.pattern}"
    )
    private String password;
}
