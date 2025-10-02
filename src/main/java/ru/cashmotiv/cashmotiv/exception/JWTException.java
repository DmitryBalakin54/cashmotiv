package ru.cashmotiv.cashmotiv.exception;

public class JWTException extends BaseException {
    public JWTException(ErrorCode errorCode) {
        super(errorCode);
    }
}
