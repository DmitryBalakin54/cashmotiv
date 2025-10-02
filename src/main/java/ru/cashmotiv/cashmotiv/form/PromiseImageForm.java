package ru.cashmotiv.cashmotiv.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PromiseImageForm {
    @NotNull(message = "{validation.userid.notnull}")
    private Long userId;

    @NotNull(message = "{validation.promise.id.notnull}")
    private Long promiseId;

    @NotNull
    private MultipartFile image;

    private String hash;
}
