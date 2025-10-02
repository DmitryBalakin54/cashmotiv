package ru.cashmotiv.cashmotiv.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cashmotiv.cashmotiv.domain.ForgotPasswordToken;

import java.util.Date;
import java.util.List;

@Repository
public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, Long> {
    ForgotPasswordToken findByToken(String token);


    ForgotPasswordToken findByUserId(Long userId);

    Page<ForgotPasswordToken> findByExpiryDateBefore(Date date, PageRequest of);
}
