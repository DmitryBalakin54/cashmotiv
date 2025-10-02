package ru.cashmotiv.cashmotiv.exception;

public class DuplicateEntryException extends DataBaseException {
    public DuplicateEntryException(ErrorCode errorCode) {
        super(errorCode);
    }
}
