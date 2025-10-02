package ru.cashmotiv.cashmotiv.repository;

import org.springframework.transaction.annotation.Transactional;import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cashmotiv.cashmotiv.domain.Email;
import ru.cashmotiv.cashmotiv.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET password=?2 WHERE id=?1", nativeQuery = true)
    void updatePassword(Long id, String password);

    User save(User user);

    @Query(value = "SELECT password FROM users WHERE login=?1", nativeQuery = true)
    String findPasswordHashByLogin(String login);

    @Query(value = "SELECT password FROM users WHERE main_email_id=?1", nativeQuery = true)
    String findPasswordHashByEmail(Long emailId);

    @Query(value = "SELECT password FROM users WHERE id=?1", nativeQuery = true)
    String findPasswordHashById(Long userId);


    User findByMainEmail(Email email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET main_email_is_verified=true WHERE id=?1", nativeQuery = true)
    void verify(Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET main_email_id=?2 WHERE id=?1", nativeQuery = true)
    void updateEmail(Long userId, Long emailId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET notifications_enabled=?1 WHERE id=?2", nativeQuery = true)
    void setNotification(Boolean notification, Long id);
}
