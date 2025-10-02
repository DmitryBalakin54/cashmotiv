package ru.cashmotiv.cashmotiv.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.cashmotiv.cashmotiv.domain.Email;
import ru.cashmotiv.cashmotiv.domain.EmailVerificationToken;
import ru.cashmotiv.cashmotiv.domain.ForgotPasswordToken;
import ru.cashmotiv.cashmotiv.domain.Promise;

@Service
public class EmailSenderService {

    private final JavaMailSender mailSender;

    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.properties.mail.from}")
    private String from;

    @Value("${app.verify.prefix.url}")
    private String verificationPrefix;

    @Value("${app.forgot.password.prefix.url}")
    private String forgotPasswordPrefix;

    public void sendTestEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendEmailVerificationEmail(String to, EmailVerificationToken token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Email Verification");
        message.setText(verificationPrefix + token.getToken());
        mailSender.send(message);
    }

    public void sendChangePasswordEmail(Email emailToSend, ForgotPasswordToken token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(emailToSend.getEmail());
        message.setSubject("Change Password");
        message.setText(forgotPasswordPrefix + token.getToken());
        mailSender.send(message);
    }

    @Async
    public void sendPromiseNotification(Email email, Promise promise) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email.getEmail());
        message.setSubject("Promise Notification");

        String emailBody = String.format(
                "Hello,\n\n" +
                        "This is a reminder about your promise:\n\n" +
                        "Title: %s\n" +
                        "Description: %s\n" +
                        "Deadline: %s\n\n" +
                        "Please complete it before the deadline.\n\n" +
                        "Best regards,\nCashMotiv Team",
                promise.getTitle(),
                promise.getDescription(),
                promise.getExpiryDate(),
                Promise.format.format(promise.getExpiryDate())
        );

        message.setText(emailBody);
        mailSender.send(message);
    }
}
