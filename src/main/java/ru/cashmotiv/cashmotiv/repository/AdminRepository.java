package ru.cashmotiv.cashmotiv.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.cashmotiv.cashmotiv.domain.Admin;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByLogin(String login);

    @Query("""
    SELECT a
    FROM Admin a
    WHERE (
    (:isActiveEnabled = true) OR
    (:notIsActiveEnabled = true)
    )
    ORDER BY
        CASE WHEN :sort IS NULL THEN a.id END ASC,
        CASE WHEN :sort LIKE '%creation_time%' AND :sort LIKE '%ASC%' THEN a.creationTime END ASC,
        CASE WHEN :sort LIKE '%creation_time%' AND :sort LIKE '%DESC%' THEN a.creationTime END DESC,
        CASE WHEN :sort LIKE '%login%' AND :sort LIKE '%ASC%' THEN a.login END ASC,
        CASE WHEN :sort LIKE '%login%' AND :sort LIKE '%DESC%' THEN a.login END DESC            
    """)
    Page<Admin> findAdminsByFilters(
            @Param("isActiveEnabled") Boolean isActiveEnabled,
            @Param("notIsActiveEnabled") Boolean notIsActiveEnabled,
            @Param("sort") String sort,
            Pageable of
    );
}
