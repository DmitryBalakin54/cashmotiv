package ru.cashmotiv.cashmotiv.dto;

import lombok.Getter;
import lombok.Setter;
import ru.cashmotiv.cashmotiv.domain.MailingEmail;

import java.util.Date;

@Getter
@Setter
public class MailingEmailDto {
    private Long id;
    private String email;
    private String unsubscribeToken;
    private Date creationTime;

    public MailingEmailDto(MailingEmail mailingEmail) {
        this.id = mailingEmail.getId();
        this.email = mailingEmail.getEmail();
        this.unsubscribeToken = mailingEmail.getUnsubscribeToken();
        this.creationTime = mailingEmail.getCreationTime();
    }
}
