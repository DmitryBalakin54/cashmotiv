package ru.cashmotiv.cashmotiv.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(indexes = {
        @Index(columnList = "expiryDate")
})
public class Promise {
    public static final SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private static final int CLOSE_MINUTES = 10;
    private static final int NOTIFICATION_MINUTES = 30;

    @Getter
    public enum PromiseType {
        NOTICE("NOTICE"),
        IMAGE("IMAGE");

        private final String type;

        PromiseType(String type) {
            this.type = type;
        }
    }

    @Getter
    public enum Status {
        IN_PROGRESS("IN_PROGRESS"),
        UNDER_REVIEW("UNDER_REVIEW"),
        VERIFIED("VERIFIED"),
        FAILED("FAILED"),
        CLOSED("CLOSED");

        private String status;

        Status(String status) {
            this.status = status;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    @Column(length = 1000)
    private String description;

    @NotNull
    private Boolean fulfilled = false;

    @NotNull
    private Boolean closed = false;

    @Column(length = 2048)
    private String proof;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PromiseType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status = Status.IN_PROGRESS;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date closeDate;

    private Boolean notification;

    @Temporal(TemporalType.TIMESTAMP)
    private Date notificationDate;

    @NotNull
    @Column(columnDefinition = "boolean default false")
    private Boolean notificationSent = false;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public boolean isExpired() {
        return new Date().after(this.expiryDate);
    }

    public static Date parseDate(String date) throws ParseException {
        return format.parse(date);
    }

    @PrePersist
    protected void calculateDates() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(creationTime != null ? creationTime : new Date());
        cal.add(Calendar.MINUTE, CLOSE_MINUTES);
        closeDate = cal.getTime();

        if (notificationDate == null) {
            cal.setTime(expiryDate);
            cal.add(Calendar.MINUTE, -NOTIFICATION_MINUTES);
            notificationDate = cal.getTime();
        }
    }
}
