package ru.cashmotiv.cashmotiv.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddOrDeleteEmailForm {

    @NotBlank(message = "{validation.email.notblank}")
    @Email(message = "{validation.email.email}")
    private String email;

    @NotNull(message = "{validation.userid.notnull}")
    private Long userId;

    private String hash;
}
