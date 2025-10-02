package ru.cashmotiv.cashmotiv.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.digester.annotations.rules.SetTop;
import ru.cashmotiv.cashmotiv.util.PromisePageRequest;

@Getter
@Setter
public class PromisePageRequestForm {

    @NotNull(message = "")
    private Long userId;

    @NotNull(message = "")
    private Integer pageNum;

    @NotNull(message = "")
    private Integer pageSize;

    @NotNull(message = "")
    private Boolean noticeEnabled;

    @NotNull(message = "")
    private Boolean imageEnabled;

    @NotNull(message = "")
    private Boolean inProgressEnabled;

    @NotNull(message = "")
    private Boolean underReviewEnabled;

    @NotNull(message = "")
    private Boolean verifiedEnabled;

    @NotNull(message = "")
    private Boolean failedEnabled;

    @NotNull(message = "")
    private Boolean closedEnabled;

    private PromisePageRequest.SortBy sortBy;
    private PromisePageRequest.SortDirection sortDirection;

    private String hash;
}
