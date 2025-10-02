package ru.cashmotiv.cashmotiv.exception;

public class ChangeEmailException extends BaseException {
    public ChangeEmailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
