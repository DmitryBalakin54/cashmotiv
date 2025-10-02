package ru.cashmotiv.cashmotiv.exception;

public class NotFoundEntityException extends DataBaseException {

    public NotFoundEntityException(ErrorCode errorCode) {
        super(errorCode);
    }
}
