package ru.cashmotiv.cashmotiv.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(indexes = {
        @Index(unique = true, columnList = "token")
})
public class EmailVerificationToken {

    private static final int EXPIRATION_HOURS = 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "email_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Email email;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @PrePersist
    protected void calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(creationTime != null ? creationTime : new Date());
        cal.add(Calendar.HOUR_OF_DAY, EXPIRATION_HOURS);
        expiryDate = cal.getTime();
    }

    public boolean isExpired() {
        return new Date().after(this.expiryDate);
    }
}
