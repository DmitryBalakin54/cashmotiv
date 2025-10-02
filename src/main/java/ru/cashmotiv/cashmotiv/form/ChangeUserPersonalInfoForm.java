package ru.cashmotiv.cashmotiv.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUserPersonalInfoForm {

    @NotNull(message = "{validation.userid.notnull}")
    private Long userId;

    @NotBlank(message = "{validation.name.notblank}")
    @Size(min = 1, max = 20, message = "{validation.name.size}")
    @Pattern(regexp = "^[\\p{L}'-]+(?:\\s[\\p{L}'-]+)*$", message = "{validation.name.pattern}")
    private String name;

    @NotBlank(message = "{validation.surname.notblank}")
    @Size(min = 1, max = 40, message = "{validation.surname.size}")
    @Pattern(regexp = "^[\\p{L}'-]+(?:\\s[\\p{L}'-]+)*$", message = "{validation.surname.pattern}")
    private String surname;

    private String hash;
}
