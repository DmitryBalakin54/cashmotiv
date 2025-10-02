package ru.cashmotiv.cashmotiv.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cashmotiv.cashmotiv.domain.Email;
import ru.cashmotiv.cashmotiv.domain.EmailVerificationToken;
import ru.cashmotiv.cashmotiv.domain.ForgotPasswordToken;
import ru.cashmotiv.cashmotiv.domain.User;
import ru.cashmotiv.cashmotiv.repository.ForgotPasswordTokenRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ForgotPasswordTokenService {

    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;

    public ForgotPasswordTokenService(ForgotPasswordTokenRepository forgotPasswordTokenRepository) {
        this.forgotPasswordTokenRepository = forgotPasswordTokenRepository;
    }

    @Transactional
    public ForgotPasswordToken save(ForgotPasswordToken token) {
        return forgotPasswordTokenRepository.save(token);
    }

    @Transactional
    public ForgotPasswordToken create(User user) {
        ForgotPasswordToken token = new ForgotPasswordToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        ForgotPasswordToken saved = forgotPasswordTokenRepository.save(token);
        // TODO разобраться что делать что происходит в случае когда связь oneToOne и создаем новый токе, ПРОВЕРИТЬ ДЛЯ ВСЕХ ТОКЕНОВ И ВСЕГО ТКОГО
        // TODO так же проверить что будет если uuid совпадает
        return saved;
    }

    @Transactional
    public ForgotPasswordToken findByToken(String token) {
        return forgotPasswordTokenRepository.findByToken(token);
    }

    @Transactional
    public void delete(ForgotPasswordToken token) {
        forgotPasswordTokenRepository.delete(token);
    }

    @Transactional
    public ForgotPasswordToken findByUser(User user) {
        return forgotPasswordTokenRepository.findByUserId(user.getId());
    }


    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void cleanupExpiredTokens() {
        System.out.println("Cleaning up expired ForgotPasswordToken");
        int page = 0;
        int pageSize = 100;
        boolean hasMore = true;

        while (hasMore) {
            Page<ForgotPasswordToken> tokenPage = forgotPasswordTokenRepository
                    .findByExpiryDateBefore(
                            new Date(),
                            PageRequest.of(page, pageSize)
                    );

            if (tokenPage.isEmpty()) {
                break;
            }

            List<ForgotPasswordToken> tokens = tokenPage.getContent();
            if (!tokens.isEmpty()) {
                forgotPasswordTokenRepository.deleteAll(tokens);
            }

            hasMore = tokenPage.hasNext();
            page++;
        }
    }
}
