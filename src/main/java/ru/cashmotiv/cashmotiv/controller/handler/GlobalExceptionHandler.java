package ru.cashmotiv.cashmotiv.controller.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.cashmotiv.cashmotiv.exception.BaseException;
import ru.cashmotiv.cashmotiv.exception.ErrorCode;
import java.util.Locale;
import java.util.function.Supplier;


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(
            BaseException ex,
            WebRequest request
    ) {
        String acceptLanguage = request.getHeader("Accept-Language");
        Locale locale = acceptLanguage != null && !acceptLanguage.trim().isEmpty()
                ? Locale.forLanguageTag(acceptLanguage.split(",")[0].trim())
                : Locale.ENGLISH;

        return ResponseEntity
                .status(resolveHttpStatus(ex.getErrorCode()))
                .body(new ErrorResponse(
                        ex.getErrorCode().getCode(),
                        resolveMessage(ex.getErrorCode(), locale),
                        ex.getDetails()
                ));
    }

    private String resolveMessage(ErrorCode errorCode, Locale locale) {
        return messageSource.getMessage(
                errorCode.getDefaultMessage(),
                null,
                errorCode.getDefaultMessage(),
                locale
        );
    }

    private HttpStatus resolveHttpStatus(ErrorCode errorCode) {
        int firstDigit = errorCode.getCode() / 1000;
        return switch (firstDigit) {
            case 4 -> HttpStatus.NOT_FOUND;
            case 3 -> HttpStatus.FORBIDDEN;
            case 2 -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

}