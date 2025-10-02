package ru.cashmotiv.cashmotiv.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCreateForm {
    @NotBlank(message = "")
    @Size(min = 3, max = 25, message = "")
    private String login;
}
