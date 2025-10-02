package ru.cashmotiv.cashmotiv.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SecondaryRow;

@Getter
@Setter
public class GetAdminForm {
    @NotNull(message = "")
    private Long adminId;

    @NotNull(message = "")
    private Long targetAdminId;

    private String hash;
}
