package ru.cashmotiv.cashmotiv.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordForm {
    @NotBlank(message = "{validation.loginoremail.notblank}")
    private String loginOrEmail;

}
