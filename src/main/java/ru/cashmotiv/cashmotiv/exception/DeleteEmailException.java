package ru.cashmotiv.cashmotiv.exception;

public class DeleteEmailException extends BaseException {
    public DeleteEmailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
