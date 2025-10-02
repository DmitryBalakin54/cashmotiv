package ru.cashmotiv.cashmotiv.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.cashmotiv.cashmotiv.domain.Email;
import ru.cashmotiv.cashmotiv.domain.EmailVerificationToken;
import ru.cashmotiv.cashmotiv.domain.User;
import ru.cashmotiv.cashmotiv.exception.ErrorCode;
import ru.cashmotiv.cashmotiv.exception.SaveEntityException;
import ru.cashmotiv.cashmotiv.repository.EmailRepository;
import ru.cashmotiv.cashmotiv.repository.EmailVerificationTokenRepository;
import ru.cashmotiv.cashmotiv.repository.UserRepository;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.concurrent.Executor;

@Service
public class EmailVerificationTokenService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private TransactionTemplate transactionTemplate;

    public EmailVerificationTokenService(EmailVerificationTokenRepository emailVerificationTokenRepository, EmailRepository emailRepository, UserRepository userRepository, PlatformTransactionManager transactionManager) {
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Transactional
    public EmailVerificationToken create(Email email) {
        EmailVerificationToken token = new EmailVerificationToken();
        token.setEmail(email);
        token.setToken(UUID.randomUUID().toString());
        // TODO подумать а может ли быть такое что оно совпадет с тем что итак уже есть и будет попа, так же подумать над подобным в других местах
        return emailVerificationTokenRepository.save(token);
    }

    @Transactional
    public EmailVerificationToken findByToken(String token) {
        return emailVerificationTokenRepository.findByToken(token);
    }

    @Transactional
    public void delete(EmailVerificationToken emailToken) {
        emailToken.getEmail().setVerificationToken(null);
        Email email = emailRepository.save(emailToken.getEmail());
        if (email == null){
            throw new SaveEntityException(ErrorCode.CANT_SAVE_EMAIL);
        }
    }

    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void cleanupExpiredTokens() {
        System.out.println("Cleaning up expired tokens(EmailVerificationToken)");
        int page = 0;
        final int pageSize = 100;
        boolean hasMore = true;

        while (hasMore) {
            final int pageNum = page;
            Page<EmailVerificationToken> tokenPage = emailVerificationTokenRepository
                    .findByExpiryDateBefore(new Date(), PageRequest.of(pageNum, pageSize));

            if (tokenPage.isEmpty()) {
                 break;
            }

            for (EmailVerificationToken token : tokenPage.getContent()) {
                processToken(token);
            }

            hasMore = tokenPage.hasNext();

            page++;
        }
    }

    @Transactional
    public void processToken(EmailVerificationToken token) {
        Email email = token.getEmail();
        if (email == null) {
            emailVerificationTokenRepository.delete(token);
            return;
        }

        User user = email.getUser();
        boolean isMainEmail = email.getIsMain();

        if (!email.getIsVerified() && !isMainEmail) {
            emailRepository.delete(email);
        } else if (user != null && isMainEmail && !email.getIsVerified()) {
            userRepository.delete(user);
        }
    }
}
