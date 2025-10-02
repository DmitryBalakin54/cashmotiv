package ru.cashmotiv.cashmotiv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cashmotiv.cashmotiv.domain.MailingEmail;

@Repository
public interface MailingEmailRepository extends JpaRepository<MailingEmail, Long> {

    MailingEmail findByEmail(String email);

    MailingEmail findByUnsubscribeToken(String unsubscribeToken);
}
