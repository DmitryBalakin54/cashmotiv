package ru.cashmotiv.cashmotiv.exception;

public class PromiseActionException extends BaseException {
    public PromiseActionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
