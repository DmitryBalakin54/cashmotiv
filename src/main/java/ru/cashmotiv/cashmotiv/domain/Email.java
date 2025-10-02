package ru.cashmotiv.cashmotiv.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(indexes = {@Index(unique = true, columnList = "email")})
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String email;

    @NotNull
    private Boolean isMain = false;

    @NotNull
    private Boolean isVerified = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "email", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "verification_token_id")
    private EmailVerificationToken verificationToken;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
}
