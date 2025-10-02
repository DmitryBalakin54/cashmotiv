package ru.cashmotiv.cashmotiv.dto;

import lombok.Getter;
import lombok.Setter;
import ru.cashmotiv.cashmotiv.domain.Admin;

import java.util.Date;

@Getter
@Setter
public class AdminDto {
    private Long id;
    private String login;
    private Boolean isActive;
    private Date creationTime;

    public AdminDto(Admin admin) {
        this.id = admin.getId();
        this.login = admin.getLogin();
        this.isActive = admin.getIsActive();
        this.creationTime = admin.getCreationTime();
    }
}
