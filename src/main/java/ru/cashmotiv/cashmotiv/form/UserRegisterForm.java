package ru.cashmotiv.cashmotiv.form;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterForm {
    @NotBlank(message = "{validation.login.notblank}")
    @Size(min = 6, max = 16, message = "{validation.login.size}")
    @Pattern(regexp = "^[A-Za-z\\d]+$", message = "{validation.login.pattern}")
    private String login;

    @NotBlank(message = "{validation.password.notblank}")
    @Size(min = 10, max = 25, message = "{validation.password.size}")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{10,25}$",
            message = "{validation.password.pattern}"
    )
    private String password;

    @NotBlank(message = "{validation.email.notblank}")
    @Email(message = "{validation.email.email}")
    private String email;
}
