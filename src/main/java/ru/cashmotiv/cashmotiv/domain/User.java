package ru.cashmotiv.cashmotiv.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users", indexes = {
        @Index(unique = true, columnList = "login"),
        @Index(unique = true, columnList = "main_email_id")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    @NotBlank
    @Column(unique = true)
    private String login;

    @OneToOne
    @JoinColumn(name = "main_email_id", unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Email mainEmail;

    @NotNull
    private Boolean mainEmailIsVerified = false;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Email> emails;

    @NotNull
    private Boolean notificationsEnabled = true;

    public void addEmails(List<Email> newEmails) {
        if (this.emails == null) {
            this.emails = new ArrayList<>();
        }

        emails.addAll(newEmails);
    }
}
