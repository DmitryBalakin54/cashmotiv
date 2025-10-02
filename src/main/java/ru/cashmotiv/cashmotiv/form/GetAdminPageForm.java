package ru.cashmotiv.cashmotiv.form;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.cashmotiv.cashmotiv.util.AdminPageRequest;

@Getter
@Setter
public class GetAdminPageForm {
    @NotNull(message = "")
    private Long adminId;

    @NotNull(message = "")
    private Integer pageNum;

    @NotNull(message = "")
    private Integer pageSize;

    @NotNull(message = "")
    private Boolean isActiveEnabled;

    @NotNull(message = "")
    private Boolean notIsActiveEnabled;

    private AdminPageRequest.SortBy sortBy;
    private AdminPageRequest.SortDirection sortDirection;

    private String hash;
}
