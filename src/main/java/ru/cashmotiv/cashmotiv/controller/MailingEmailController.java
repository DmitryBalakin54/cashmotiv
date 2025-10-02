package ru.cashmotiv.cashmotiv.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.cashmotiv.cashmotiv.domain.MailingEmail;
import ru.cashmotiv.cashmotiv.dto.MailingEmailDto;
import ru.cashmotiv.cashmotiv.exception.ErrorCode;
import ru.cashmotiv.cashmotiv.exception.SaveEntityException;
import ru.cashmotiv.cashmotiv.exception.ValidationException;
import ru.cashmotiv.cashmotiv.form.MailingEmailForm;
import ru.cashmotiv.cashmotiv.service.MailingEmailService;

@RestController
@RequestMapping("/api/mailing")
public class MailingEmailController {

    private final MailingEmailService mailingEmailService;

    public MailingEmailController(MailingEmailService mailingEmailService) {
        this.mailingEmailService = mailingEmailService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<MailingEmailDto> subscribe(
            @RequestBody @Valid MailingEmailForm form,
            BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        MailingEmail email = mailingEmailService.findByEmail(form.getEmail());
        if (email != null) {
            return ResponseEntity.ok(new MailingEmailDto(email));
        }

        email = mailingEmailService.create(form.getEmail());
        if (email == null) {
            throw new SaveEntityException(ErrorCode.CANT_SAVE_MAILING_EMAIL);
        }

        return ResponseEntity.ok(new MailingEmailDto(email));
    }

    @GetMapping("/unsubscribe")
    public String unsubscribe(@RequestParam("token") String token) {
        if (token == null || token.isBlank()) {
            return null;
        }

        mailingEmailService.unsubscribe(token);
        return "Success";
    }
}
