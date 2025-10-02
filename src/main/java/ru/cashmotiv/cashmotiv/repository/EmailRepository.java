package ru.cashmotiv.cashmotiv.repository;

import org.springframework.transaction.annotation.Transactional;import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cashmotiv.cashmotiv.domain.Email;
import ru.cashmotiv.cashmotiv.domain.EmailVerificationToken;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    @Query(value = "SELECT * FROM email WHERE email=?1", nativeQuery = true)
    Email findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE email SET is_verified=true WHERE id=?1", nativeQuery = true)
    void verifyById(Long id);

//    @Modifying
//    @Transactional
//    @Query(value = "UPDATE email SET verification_token_id=?2 WHERE id=?1", nativeQuery = true)
//    void updateToken(Long email, Long token);

//    @Modifying
//    @Transactional
//    @Query(value = "UPDATE email SET verification_token_id=null WHERE id=?1", nativeQuery = true)
//    void removeToken(Long id);
}
