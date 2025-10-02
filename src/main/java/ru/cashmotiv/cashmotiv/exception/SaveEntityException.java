package ru.cashmotiv.cashmotiv.exception;

public class SaveEntityException extends DataBaseException{
    public SaveEntityException(ErrorCode errorCode) {
        super(errorCode);
    }
}
