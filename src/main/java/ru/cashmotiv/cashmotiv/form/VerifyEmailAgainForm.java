package ru.cashmotiv.cashmotiv.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyEmailAgainForm {
    @NotBlank(message = "{validation.email.notblank}")
    @Email(message = "{validation.email.email}")
    private String email;
}
