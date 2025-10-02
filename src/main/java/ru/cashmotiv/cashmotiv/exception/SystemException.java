package ru.cashmotiv.cashmotiv.exception;

public class SystemException extends BaseException {
    public SystemException(ErrorCode errorCode) {
        super(errorCode);
    }
}
