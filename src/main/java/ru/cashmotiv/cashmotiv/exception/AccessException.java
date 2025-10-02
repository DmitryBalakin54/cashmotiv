package ru.cashmotiv.cashmotiv.exception;

public class AccessException extends BaseException {
    public AccessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
