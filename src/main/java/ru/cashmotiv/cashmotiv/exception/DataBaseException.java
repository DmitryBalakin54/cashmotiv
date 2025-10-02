package ru.cashmotiv.cashmotiv.exception;

public class DataBaseException extends BaseException {
    public DataBaseException(ErrorCode errorCode) {
        super(errorCode);
    }
}
