package ru.cashmotiv.cashmotiv.exception;

public class UserLogInException extends BaseException {
    public UserLogInException(ErrorCode errorCode) {
        super(errorCode);
    }
}
