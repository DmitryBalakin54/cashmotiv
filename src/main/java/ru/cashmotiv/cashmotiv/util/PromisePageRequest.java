package ru.cashmotiv.cashmotiv.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.cashmotiv.cashmotiv.form.PromisePageRequestForm;

import java.util.Map;

@Getter
@Setter
public class PromisePageRequest {

    private Long userId;
    private Integer pageNum;
    private Integer pageSize;
    private Boolean noticeEnabled;
    private Boolean imageEnabled;
    private Boolean inProgressEnabled;
    private Boolean underReviewEnabled;
    private Boolean verifiedEnabled;
    private Boolean failedEnabled;
    private Boolean closedEnabled;
    private String sort;

    public PromisePageRequest(PromisePageRequestForm form) {
        this.userId = form.getUserId();
        this.pageNum = form.getPageNum();
        this.pageSize = form.getPageSize();
        this.noticeEnabled = form.getNoticeEnabled();
        this.imageEnabled = form.getImageEnabled();
        this.inProgressEnabled = form.getInProgressEnabled();
        this.underReviewEnabled = form.getUnderReviewEnabled();
        this.verifiedEnabled = form.getVerifiedEnabled();
        this.failedEnabled = form.getFailedEnabled();
        this.closedEnabled = form.getClosedEnabled();
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
        EXPIRATION_DATE
    }

    private final Map<SortBy, String> sortByConvert =
            Map.of(
                    SortBy.CREATION_DATE, "creation_time",
                    SortBy.EXPIRATION_DATE, "expire_date"
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
