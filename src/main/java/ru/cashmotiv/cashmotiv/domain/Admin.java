package ru.cashmotiv.cashmotiv.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(indexes = {
        @Index(unique = true, columnList = "login")
})
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String login;

    @NotNull
    private Boolean isActive = false;

    @NotBlank
    private String accessToken;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
}
