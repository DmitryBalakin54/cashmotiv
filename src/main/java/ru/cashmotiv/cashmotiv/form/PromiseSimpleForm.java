package ru.cashmotiv.cashmotiv.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromiseSimpleForm {

    @NotNull(message = "{validation.userid.notnull}")
    private Long userId;

    @NotNull(message = "{validation.promise.id.notnull}")
    private Long promiseId;

    private String hash;
}
