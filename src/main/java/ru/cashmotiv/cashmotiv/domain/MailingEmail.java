package ru.cashmotiv.cashmotiv.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(indexes = {
        @Index(unique = true, columnList = "email"),
        @Index(unique = true, columnList = "unsubscribe_token")
})
public class MailingEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Column(unique = true)
    private String unsubscribeToken;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
}
