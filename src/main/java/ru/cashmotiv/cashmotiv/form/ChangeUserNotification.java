package ru.cashmotiv.cashmotiv.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUserNotification {

    @NotNull(message = "{validation.userid.notnull}")
    private Long userId;

    @NotNull(message = "{validation.notification.notnull}")
    private Boolean notification;

    private String hash;
}
