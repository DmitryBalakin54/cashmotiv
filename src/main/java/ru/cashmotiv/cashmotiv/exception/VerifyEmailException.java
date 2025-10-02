package ru.cashmotiv.cashmotiv.exception;

public class VerifyEmailException extends BaseException {
    public VerifyEmailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
