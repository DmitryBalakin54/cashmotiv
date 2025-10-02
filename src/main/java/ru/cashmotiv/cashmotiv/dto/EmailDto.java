package ru.cashmotiv.cashmotiv.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.cashmotiv.cashmotiv.domain.Email;
import ru.cashmotiv.cashmotiv.domain.EmailVerificationToken;
import ru.cashmotiv.cashmotiv.domain.User;

import java.util.Date;

@Getter
@Setter
public class EmailDto {

    private Long id;
    private String email;
    private Boolean isMain;
    private Boolean isVerified;
    private Date creationTime;

    public EmailDto(Email email) {
        if (email == null) {
            return;
        }

        this.id = email.getId();
        this.email = email.getEmail();
        this.isMain = email.getIsMain();
        this.isVerified = email.getIsVerified();
        this.creationTime = email.getCreationTime();
    }
}
