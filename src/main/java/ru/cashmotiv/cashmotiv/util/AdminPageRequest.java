package ru.cashmotiv.cashmotiv.util;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.cashmotiv.cashmotiv.form.GetAdminPageForm;

import java.util.Map;

@Getter
@Setter
public class AdminPageRequest {
    private Integer pageNum;
    private Integer pageSize;
    private Boolean isActiveEnabled;
    private Boolean notIsActiveEnabled;
    private String sort;

    public AdminPageRequest(GetAdminPageForm form) {
        this.pageNum = form.getPageNum();
        this.pageSize = form.getPageSize();
        this.isActiveEnabled = form.getIsActiveEnabled();
        this.notIsActiveEnabled = form.getNotIsActiveEnabled();
        if (form.getSortBy() != null) {
            this.sort = sortByConvert.getOrDefault(form.getSortBy(), null);
            if (form.getSortDirection() != null && this.sort != null) {
                String sortDirection = sortDirectionConvert.getOrDefault(form.getSortDirection(), null);
                if (sortDirection == null) {
                    this.sort += " DESC";
                } else {
                    this.sort += " " + sortDirection;
                }
            }
        }
    }

    public enum SortBy {
        CREATION_DATE,
        LOGIN
    }

    private final Map<SortBy, String> sortByConvert =
            Map.of(
                    SortBy.CREATION_DATE, "creation_time",
                    SortBy.LOGIN, "login"
            );

    public enum SortDirection {
        ASC,
        DESC
    }

    private final Map<SortDirection, String> sortDirectionConvert =
            Map.of(
                    SortDirection.ASC, "ASC",
                    SortDirection.DESC, "DESC"
            );
}
