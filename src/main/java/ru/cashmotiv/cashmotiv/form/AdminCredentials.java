package ru.cashmotiv.cashmotiv.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCredentials {
    @NotBlank(message = "")
    private String login;

    @NotBlank(message = "")
    private String accessToken;
}
