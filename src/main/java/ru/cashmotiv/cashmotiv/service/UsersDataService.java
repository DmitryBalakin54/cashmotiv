package ru.cashmotiv.cashmotiv.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cashmotiv.cashmotiv.domain.Email;
import ru.cashmotiv.cashmotiv.domain.EmailVerificationToken;
import ru.cashmotiv.cashmotiv.domain.User;
import ru.cashmotiv.cashmotiv.exception.ErrorCode;
import ru.cashmotiv.cashmotiv.exception.NotFoundEntityException;
import ru.cashmotiv.cashmotiv.exception.SaveEntityException;

import java.util.List;

@Service
public class UsersDataService {

    private final UserService userService;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final EmailService emailService;
    private final EmailSenderService emailSenderService;

    public UsersDataService(UserService userService, EmailVerificationTokenService emailVerificationTokenService, EmailService emailService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailVerificationTokenService = emailVerificationTokenService;
        this.emailService = emailService;
        this.emailSenderService = emailSenderService;
    }

    @Transactional
    public Email createNewEmail(String emailName, User user, boolean isMain) {
        Email email = new Email();
        email.setUser(user);
        email.setEmail(emailName);
        email.setIsMain(isMain);
        Email newEmail = emailService.save(email);
        if (newEmail == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_EMAIL);
        }

        if (isMain) {
            user.setMainEmail(newEmail);
        }

        user.addEmails(List.of(newEmail));
        user = userService.save(user);
        if (user == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_USER);
        }

        EmailVerificationToken token = emailVerificationTokenService.create(newEmail);
        if (token == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_EMAIL_TOKEN);
        }

        emailSenderService.sendEmailVerificationEmail(newEmail.getEmail(), token);
        return newEmail;
    }

    @Transactional
    public Email changeMainEmail(User user, Email newEmail) {
        if (newEmail == null || user == null) {
            return null;
        }

        Email oldEmail = user.getMainEmail();
        oldEmail.setIsMain(false);
        newEmail.setIsMain(true);
        user.setMainEmail(newEmail);

        if (emailService.save(oldEmail) == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_EMAIL);
        }

        newEmail = emailService.save(newEmail);
        if (newEmail == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_EMAIL);
        }

        if (userService.save(user) == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_USER);
        }

        return newEmail;
    }
}
