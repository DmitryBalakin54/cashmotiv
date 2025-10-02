package ru.cashmotiv.cashmotiv.controller;

import jakarta.validation.Valid;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.cashmotiv.cashmotiv.domain.Email;
import ru.cashmotiv.cashmotiv.domain.EmailVerificationToken;
import ru.cashmotiv.cashmotiv.domain.ForgotPasswordToken;
import ru.cashmotiv.cashmotiv.domain.User;
import ru.cashmotiv.cashmotiv.dto.EmailDto;
import ru.cashmotiv.cashmotiv.dto.UserDto;
import ru.cashmotiv.cashmotiv.exception.*;
import ru.cashmotiv.cashmotiv.form.*;
import ru.cashmotiv.cashmotiv.service.*;
import ru.cashmotiv.cashmotiv.util.BlurEmail;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final EmailSenderService emailSenderService;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final UsersDataService usersDataService;
    private final ForgotPasswordTokenService forgotPasswordTokenService;
    private final UserAccessHashService userAccessHashService;

    public UserController(UserService userService, EmailService emailService, EmailSenderService emailSenderService, EmailVerificationTokenService emailVerificationTokenService, UsersDataService usersDataService, ForgotPasswordTokenService forgotPasswordTokenService, UserAccessHashService userAccessHashService) {
        this.userService = userService;
        this.emailService = emailService;
        this.emailSenderService = emailSenderService;
        this.emailVerificationTokenService = emailVerificationTokenService;
        this.usersDataService = usersDataService;
        this.forgotPasswordTokenService = forgotPasswordTokenService;
        this.userAccessHashService = userAccessHashService;
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<UserDto> register(
            @RequestBody @Valid UserRegisterForm form,
            BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }


        User user = new User();
        user.setLogin(form.getLogin());
        User registredUser = userService.register(user, form.getEmail(), form.getPassword());
        if (registredUser == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_USER);
        }

        Email email = usersDataService.createNewEmail(form.getEmail(), registredUser, true);
        return ResponseEntity.ok(new UserDto(registredUser));
    }

    @PatchMapping("/change/personal/info")
    public ResponseEntity<UserDto> changeUserPersonalInfo(
            @RequestBody @Valid ChangeUserPersonalInfoForm form,
            BindingResult bindingResult)
    {
        userAccessHashService.checkHashByUserId(form.getHash(), form.getUserId());
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        User user = userService.findById(form.getUserId());
        if (user == null) {
            throw new NotFoundEntityException(ErrorCode.NOTFOUND_USER);
        }

        user.setName(form.getName());
        user.setSurname(form.getSurname());
        user = userService.save(user);
        if (user == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_USER);
        }

        return ResponseEntity.ok(new UserDto(user));
    }

    @PatchMapping("/change/password")
    public ResponseEntity<UserDto> changeUserPassword(
            @RequestBody @Valid ChangePasswordForm form,
            BindingResult bindingResult)
    {
        userAccessHashService.checkHashByUserId(form.getHash(), form.getUserId());
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        User user = userService.findById(form.getUserId());
        if (user == null) {
            throw new NotFoundEntityException(ErrorCode.NOTFOUND_USER);
        }

        user = userService.changePassword(form.getOldPassword(), form.getNewPassword(), user);
        if (user == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_USER);
        }

        return ResponseEntity.ok(new UserDto(user));
    }


    @PostMapping("/forgot/password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordForm form) {
        Email emailToSend;
        User user;

        if (EmailValidator.getInstance().isValid(form.getLoginOrEmail())) {
            emailToSend = emailService.findByEmail(form.getLoginOrEmail());
            if (emailToSend == null) {
                throw new NotFoundEntityException(ErrorCode.INCORRECT_EMAIL);
            }

            user = emailToSend.getUser();
        } else {
            user = userService.findByLogin(form.getLoginOrEmail());
            if (user == null) {
                throw new NotFoundEntityException(ErrorCode.LOGIN_NOT_EXIST);
            }

            emailToSend = user.getMainEmail();
        }

        ForgotPasswordToken oldToken = forgotPasswordTokenService.findByUser(user);
        if (oldToken != null) {
            forgotPasswordTokenService.delete(oldToken);
        }


        ForgotPasswordToken token = forgotPasswordTokenService.create(user);
        if (token == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_PASSWORD_TOKEN);
        }

        emailSenderService.sendChangePasswordEmail(emailToSend, token);

        return ResponseEntity.ok(BlurEmail.blur(emailToSend.getEmail()));
    }

    @GetMapping("/check/forgot/password/token")
    public ResponseEntity<Boolean> checkForgotPasswordToken(
            @RequestParam(value = "token") String token)
    {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (forgotPasswordTokenService.findByToken(token) == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(true);
    }

    @PostMapping("/change/forgotten/password")
    public ResponseEntity<UserDto> createNewPassword(
            @RequestBody @Valid ChangeForgottenPasswordForm form,
            BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        ForgotPasswordToken token = forgotPasswordTokenService.findByToken(form.getToken());
        if (token == null) {
            throw new NotFoundEntityException(ErrorCode.NOTFOUND_PASSWORD_TOKEN);
        }

        User user = token.getUser();
        forgotPasswordTokenService.delete(token);

        user = userService.changePasswordUnchecked(form.getNewPassword(), user);
        if (user == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_USER);
        }

        return ResponseEntity.ok(new UserDto(user));
    }

    @PostMapping("/send/verify/email/again")
    public ResponseEntity<EmailDto> verifyEmailAgain(
            @RequestBody @Valid VerifyEmailAgainForm form,
            BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        Email email = emailService.findByEmail(form.getEmail());
        if (email == null) {
            throw new NotFoundEntityException(ErrorCode.NOTFOUND_EMAIL);
        }

        if (email.getIsVerified()) {
            throw new VerifyEmailException(ErrorCode.EMAIL_IS_ALREADY_VERIFIED);
        }

        EmailVerificationToken oldToken = email.getVerificationToken();
        if (oldToken != null) {
            emailVerificationTokenService.delete(oldToken);
        }


        EmailVerificationToken token = emailVerificationTokenService.create(email);
        if (token == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_EMAIL_TOKEN);
        }

        email.setVerificationToken(token);
        email = emailService.save(email);
        if (email == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_EMAIL);
        }

        emailSenderService.sendEmailVerificationEmail(email.getEmail(), token);

        return ResponseEntity.ok(new EmailDto(email));
    }
}
