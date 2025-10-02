package ru.cashmotiv.cashmotiv.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetAdminActiveForm {
    @NotNull(message = "")
    private Long adminId;

    @NotNull(message = "")
    private Long targetAdminId;

    @NotNull(message = "")
    private Boolean activeStatus;

    private String hash;
}
