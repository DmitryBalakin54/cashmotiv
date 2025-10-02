package ru.cashmotiv.cashmotiv.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.cashmotiv.cashmotiv.domain.Promise;
import ru.cashmotiv.cashmotiv.dto.PromiseDto;
import ru.cashmotiv.cashmotiv.util.PromisePageRequest;

import java.util.Date;

@Repository
public interface PromiseRepository extends JpaRepository<Promise, Long> {

    Page<Promise> findByClosedFalseAndExpiryDateBefore(Date date, PageRequest of);

    Page<Promise> findByClosedFalseAndNotificationTrueAndNotificationDateBeforeAndNotificationSentFalse(
            Date date, PageRequest of
    );

    @Query("""
        SELECT p
        FROM Promise p
        WHERE p.user.id = :userId
        AND (
            (:noticeEnabled = true AND p.type = 'NOTICE') OR
            (:imageEnabled = true AND p.type = 'IMAGE')
        )
        AND (
            (:inProgressEnabled = true AND p.status = 'IN_PROGRESS') OR
            (:underReviewEnabled = true AND p.status = 'UNDER_REVIEW') OR
            (:verifiedEnabled = true AND p.status = 'VERIFIED') OR
            (:failedEnabled = true AND p.status = 'FAILED') OR
            (:closedEnabled = true AND p.status = 'CLOSED')
        )
        ORDER BY
            CASE WHEN :sort IS NULL THEN p.id END ASC,
            CASE WHEN :sort LIKE '%creation_time%' AND :sort LIKE '%ASC%' THEN p.creationTime END ASC,
            CASE WHEN :sort LIKE '%creation_time%' AND :sort LIKE '%DESC%' THEN p.creationTime END DESC,
            CASE WHEN :sort LIKE '%expire_date%' AND :sort LIKE '%ASC%' THEN p.expiryDate END ASC,
            CASE WHEN :sort LIKE '%expire_date%' AND :sort LIKE '%DESC%' THEN p.expiryDate END DESC
    """)
    Page<Promise> findPromisesByFilters(
            @Param("userId") Long userId,
            @Param("noticeEnabled") Boolean noticeEnabled,
            @Param("imageEnabled") Boolean imageEnabled,
            @Param("inProgressEnabled") Boolean inProgressEnabled,
            @Param("underReviewEnabled") Boolean underReviewEnabled,
            @Param("verifiedEnabled") Boolean verifiedEnabled,
            @Param("failedEnabled") Boolean failedEnabled,
            @Param("closedEnabled") Boolean closedEnabled,
            @Param("sort") String sort,
            Pageable pageable
    );
}
