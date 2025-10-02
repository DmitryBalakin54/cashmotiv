package ru.cashmotiv.cashmotiv.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cashmotiv.cashmotiv.domain.MailingEmail;
import ru.cashmotiv.cashmotiv.exception.ErrorCode;
import ru.cashmotiv.cashmotiv.exception.SaveEntityException;
import ru.cashmotiv.cashmotiv.repository.MailingEmailRepository;

import java.beans.Transient;
import java.util.UUID;

@Service
public class MailingEmailService {

    private final MailingEmailRepository mailingEmailRepository;

    public MailingEmailService(MailingEmailRepository mailingEmailRepository) {
        this.mailingEmailRepository = mailingEmailRepository;
    }

    @Transient
    public MailingEmail create(String email) {
        MailingEmail mailingEmail = new MailingEmail();
        mailingEmail.setEmail(email);
        mailingEmail.setUnsubscribeToken(UUID.randomUUID().toString());
        return mailingEmailRepository.save(mailingEmail);
    }


    public MailingEmail findByEmail(String email) {
        return mailingEmailRepository.findByEmail(email);
    }

    @Transactional
    public void unsubscribe(String token) {
        MailingEmail email = mailingEmailRepository.findByUnsubscribeToken(token);
        if (email == null){
            throw new SaveEntityException(ErrorCode.CANT_SAVE_MAILING_EMAIL);
        }
        mailingEmailRepository.delete(email);
    }
}
