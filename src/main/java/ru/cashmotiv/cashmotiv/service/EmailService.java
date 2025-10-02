package ru.cashmotiv.cashmotiv.service;

import org.springframework.transaction.annotation.Transactional;import org.springframework.stereotype.Service;
import ru.cashmotiv.cashmotiv.domain.Email;
import ru.cashmotiv.cashmotiv.domain.EmailVerificationToken;
import ru.cashmotiv.cashmotiv.exception.ErrorCode;
import ru.cashmotiv.cashmotiv.exception.SaveEntityException;
import ru.cashmotiv.cashmotiv.repository.EmailRepository;

@Service
public class EmailService {

    private final EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public Email findByEmail(String email) {
        return emailRepository.findByEmail(email);
    }

    @Transactional
    public Email save(Email email) {
        return emailRepository.save(email);
    }

    @Transactional
    public void verify(Email email) {
        email.setVerificationToken(null);
        email.setIsVerified(true);
        email = emailRepository.save(email);
        if (email == null) {
            throw new SaveEntityException(ErrorCode.INCORRECT_EMAIL);
        }
    }

    @Transactional
    public void delete(Email email) {
        emailRepository.delete(email);
    }
}
