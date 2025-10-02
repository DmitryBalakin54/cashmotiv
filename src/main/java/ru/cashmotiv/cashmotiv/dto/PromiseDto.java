package ru.cashmotiv.cashmotiv.dto;

import lombok.Getter;
import lombok.Setter;
import ru.cashmotiv.cashmotiv.domain.Promise;

import java.util.Date;

@Getter
@Setter
public class PromiseDto {
    private Long id;
    private String title;
    private String description;
    private String type;
    private String proof;
    private String status;
    private Date expiryDate;
    private Date creationTime;
    private Long userId;

    public PromiseDto(Promise promise) {
        this.id = promise.getId();
        this.title = promise.getTitle();
        this.description = promise.getDescription();
        this.type = promise.getType().getType();
        this.proof = promise.getProof();
        this.status = promise.getStatus().getStatus();
        this.expiryDate = promise.getExpiryDate();
        this.creationTime = promise.getCreationTime();
        this.userId = promise.getUser().getId();
    }
}
