package ru.cashmotiv.cashmotiv.exception;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    protected BaseException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    protected BaseException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.name());
        this.errorCode = errorCode;
        this.details = details != null ? details : new HashMap<>();
    }

    public void addDetail(String key, Object value) {
        this.details.put(key, value);
    }
}