package ru.cashmotiv.cashmotiv.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.cashmotiv.cashmotiv.domain.Email;
import ru.cashmotiv.cashmotiv.domain.EmailVerificationToken;
import ru.cashmotiv.cashmotiv.domain.User;
import ru.cashmotiv.cashmotiv.dto.EmailDto;
import ru.cashmotiv.cashmotiv.dto.UserDto;
import ru.cashmotiv.cashmotiv.exception.*;
import ru.cashmotiv.cashmotiv.form.AddOrDeleteEmailForm;
import ru.cashmotiv.cashmotiv.form.ChangeUserNotification;
import ru.cashmotiv.cashmotiv.service.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailVerificationTokenService emailVerificationTokenService;
    private final EmailService emailService;
    private final UserService userService;
    private final UsersDataService usersDataService;
    private final UserAccessHashService userAccessHashService;

    public EmailController(EmailVerificationTokenService emailVerificationTokenService, EmailService emailService, UserService userService, UsersDataService usersDataService, UserAccessHashService userAccessHashService) {
        this.emailVerificationTokenService = emailVerificationTokenService;
        this.emailService = emailService;
        this.userService = userService;
        this.usersDataService = usersDataService;
        this.userAccessHashService = userAccessHashService;
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token) {
        EmailVerificationToken emailToken = emailVerificationTokenService.findByToken(token);
        if (emailToken == null) {
            throw new NotFoundEntityException(ErrorCode.NOTFOUND_EMAIL_TOKEN);
        }

        emailService.verify(emailToken.getEmail());
        userService.verify(emailToken.getEmail().getUser());

        // TODO
        return "Email verified";
    }

    @PostMapping("/add/new")
    public ResponseEntity<EmailDto> addEmail(
            @RequestBody @Valid AddOrDeleteEmailForm form,
            BindingResult bindingResult)
    {
        userAccessHashService.checkHashByUserId(form.getHash(), form.getUserId());
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        if (emailService.findByEmail(form.getEmail()) != null) {
            throw new DuplicateEntryException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = userService.findById(form.getUserId());
        if (user == null) {
            throw new NotFoundEntityException(ErrorCode.NOTFOUND_USER);
        }

        Email newEmail = usersDataService.createNewEmail(form.getEmail(), user, false);

        return ResponseEntity.ok(new EmailDto(newEmail));
    }

    @PatchMapping("/change/main/email")
    public ResponseEntity<EmailDto> changeMainEmail(
            @RequestBody @Valid AddOrDeleteEmailForm form,
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

        Email newEmail = emailService.findByEmail(form.getEmail());
        if (newEmail == null) {
            throw new NotFoundEntityException(ErrorCode.NOTFOUND_EMAIL);
        }

        if (!newEmail.getUser().getId().equals(user.getId())) {
            throw new ChangeEmailException(ErrorCode.INCORRECT_EMAIL);
        }

        if (!newEmail.getIsVerified()) {
            throw new ChangeEmailException(ErrorCode.EMAIL_IS_NOT_VERIFIED);
        }

        if (newEmail.getIsMain()) {
            throw new ChangeEmailException(ErrorCode.EMAIL_IS_ALREADY_MAIN);
        }

        Email newMainEmail = usersDataService.changeMainEmail(user, newEmail);
        if (newMainEmail == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_EMAIL);
        }

        return ResponseEntity.ok(new EmailDto(newMainEmail));
    }

    @PatchMapping("/change/notification")
    public ResponseEntity<UserDto> changeNotice(
            @RequestBody @Valid ChangeUserNotification form,
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

        user = userService.setNotification(form.getNotification(), user);
        if (user == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_USER);
        }

        return ResponseEntity.ok(new UserDto(user));
    }

    @DeleteMapping("/delete/email")
    public ResponseEntity<Boolean> deleteEmail(
            @RequestBody @Valid AddOrDeleteEmailForm form,
            BindingResult bindingResult)
    {
        userAccessHashService.checkHashByUserId(form.getHash(), form.getUserId());
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        Email email = emailService.findByEmail(form.getEmail());
        if (email == null) {
            throw new NotFoundEntityException(ErrorCode.NOTFOUND_EMAIL);
        }

        if (!email.getUser().getId().equals(form.getUserId())) {
            throw new DeleteEmailException(ErrorCode.INCORRECT_EMAIL_ID);
        }

        if (email.getIsMain()) {
            throw new DeleteEmailException(ErrorCode.CANT_DELETE_MAIN_EMAIL);
        }

        emailService.delete(email);

        return ResponseEntity.ok(true);
    }
}
