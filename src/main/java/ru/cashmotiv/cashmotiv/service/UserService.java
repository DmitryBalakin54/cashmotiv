package ru.cashmotiv.cashmotiv.service;

import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.cashmotiv.cashmotiv.domain.Email;
import ru.cashmotiv.cashmotiv.domain.User;
import ru.cashmotiv.cashmotiv.exception.*;
import ru.cashmotiv.cashmotiv.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Transactional
    public User register(User user, String email, String password) {
        if (findByLogin(user.getLogin()) != null) {
            throw new DuplicateEntryException(ErrorCode.DUPLICATE_USER);
        }

        if (emailService.findByEmail(email) != null) {
            throw new DuplicateEntryException(ErrorCode.DUPLICATE_EMAIL);
        }

        User newUser = userRepository.save(user);
        if (newUser != null) {
            changePasswordUnchecked(password, user);
        }

        return newUser;
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }


    private User findByLoginAndPassword(String login, String password) {
        String truePassword = userRepository.findPasswordHashByLogin(login);
        if (truePassword == null) {
            throw new UserLogInException(ErrorCode.LOGIN_NOT_EXIST);
        }

        if (checkPassword(password, truePassword)) {
            return userRepository.findByLogin(login);
        }

        throw new UserLogInException(ErrorCode.INCORRECT_PASSWORD);
    }


    private User findByEmailAndPassword(String email, String password) {
        Email realEmail = emailService.findByEmail(email);
        if (realEmail == null) {
            throw new UserLogInException(ErrorCode.NOTFOUND_EMAIL);
        }

        String truePassword = userRepository.findPasswordHashByEmail(realEmail.getUser().getMainEmail().getId());
        if (truePassword == null) {
            throw new UserLogInException(ErrorCode.NOTFOUND_EMAIL);
        }

        if (checkPassword(password, truePassword)) {
            return userRepository.findByMainEmail(realEmail);
        }

        throw new UserLogInException(ErrorCode.INCORRECT_PASSWORD);
    }

    private Boolean checkPassword(String password, String truePassword) {
        return BCrypt.checkpw(password, truePassword);
    }

    @Transactional
    public User changePassword(String oldPassword, String newPassword, User user) {
        String truePassword = userRepository.findPasswordHashById(user.getId());
        if (truePassword == null) {
           throw new NotFoundEntityException(ErrorCode.NOTFOUND_USER);
        }

        if (!BCrypt.checkpw(oldPassword, truePassword)) {
            throw new UserLogInException(ErrorCode.INCORRECT_PASSWORD);
        }

        return changePasswordUnchecked(newPassword, user);
    }

    @Transactional
    public User changePasswordUnchecked(String newPassword, User user) {
        userRepository.updatePassword(user.getId(), BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        User newUser = userRepository.findById(user.getId()).orElse(null);
        return newUser;
    }

    @Transactional
    public User findByLoginOrEmailAndPassword(String loginOrEmail, String password) {
        if (loginOrEmail == null || password == null) {
            return null;
        }

        if (EmailValidator.getInstance().isValid(loginOrEmail)) {
            return findByEmailAndPassword(loginOrEmail, password);
        }

        return findByLoginAndPassword(loginOrEmail, password);
    }

    @Transactional
    public void verify(User user) {
        if (user == null) {
            return;
        }

        user.setMainEmailIsVerified(true);
        if (save(user) == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_USER);
        }
    }

    @Transactional
    public User findById(Long userId) {
        if (userId == null) {
            return null;
        }

        return userRepository.findById(userId).orElse(null);
    }

    @Transactional
    public User save(User user) {
        if (user == null) {
            return null;
        }

        return userRepository.save(user);
    }

    @Transactional
    public User setNotification(Boolean notification, User user) {
        user.setNotificationsEnabled(notification);
        return save(user);
    }

    @Transactional
    public User findByEmail(String email) {
        Email realEmail = emailService.findByEmail(email);
        if (realEmail == null) {
            throw new NotFoundEntityException(ErrorCode.NOTFOUND_EMAIL);
        }

        return userRepository.findByMainEmail(realEmail);
    }
}
