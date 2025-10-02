package ru.cashmotiv.cashmotiv.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cashmotiv.cashmotiv.domain.EmailVerificationToken;

import java.util.Date;
import java.util.List;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    void deleteByExpiryDateBefore(Date expiryDate);

    EmailVerificationToken findByToken(String token);

    Page<EmailVerificationToken> findByExpiryDateBefore(Date date, PageRequest of);
}
