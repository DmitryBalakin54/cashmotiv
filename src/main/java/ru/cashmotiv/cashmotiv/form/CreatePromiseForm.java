package ru.cashmotiv.cashmotiv.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePromiseForm {

    @NotNull(message = "{validation.userid.notnull}")
    private Long userId;

    @NotBlank(message = "{validation.promise.title.notblank}")
    @Size(min = 3, max = 50, message = "{validation.promise.title.size}")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9\\s\"'`.,!?-]+$", message = "{validation.promise.title.pattern}")
    private String title;

    @NotBlank(message = "{validation.promise.description.notblank}")
    @Size(min = 10, max = 1000, message = "{validation.promise.description.size}")
    private String description;

    @NotBlank(message = "{validation.promise.deadline.notblank}")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[+-]\\d{2}:\\d{2}$", message = "{validation.promise.deadline.pattern}")
    private String deadline;

    @NotBlank(message = "{validation.promise.type.notblank}")
    @Pattern(regexp = "IMAGE|NOTICE", message = "{validation.promise.type.pattern}")
    private String type;

    @NotNull(message = "{validation.promise.notification.notnull}")
    private Boolean notification;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[+-]\\d{2}:\\d{2}$", message = "{validation.promise.notificationTIme.pattern}")
    private String notificationTime;

    private String hash;
}
