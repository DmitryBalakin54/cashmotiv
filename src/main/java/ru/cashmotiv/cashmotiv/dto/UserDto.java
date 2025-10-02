package ru.cashmotiv.cashmotiv.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.cashmotiv.cashmotiv.domain.Email;
import ru.cashmotiv.cashmotiv.domain.User;

import java.util.Date;
import java.util.List;


@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private String login;
    private EmailDto mainEmail;
    private Boolean notificationsEnabled;
    private Boolean mainEmailIsVerified;
    private Date creationTime;
    private List<EmailDto> emails;

    public UserDto(User user) {
        if (user == null) {
            return;
        }

        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.login = user.getLogin();
        this.mainEmail = new EmailDto(user.getMainEmail());
        this.mainEmailIsVerified = user.getMainEmailIsVerified();
        this.creationTime = user.getCreationTime();
        this.notificationsEnabled = user.getNotificationsEnabled();
        if (user.getEmails() != null) {
            this.emails = user.getEmails().stream()
                    .map((email -> new EmailDto(email)))
                    .toList();
        }
    }
}
