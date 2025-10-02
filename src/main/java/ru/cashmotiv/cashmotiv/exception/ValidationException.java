package ru.cashmotiv.cashmotiv.exception;

import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ValidationException extends BaseException {

    public ValidationException(ErrorCode errorCode, String fieldName) {
        super(errorCode);
        addDetail("field", fieldName);
    }

    public ValidationException(ErrorCode errorCode, String fieldName, Object rejectedValue) {
        this(errorCode, fieldName);
        addDetail("rejectedValue", rejectedValue);
    }

    public ValidationException(BindingResult bindingResult) {
        super(ErrorCode.VALIDATION_FAILED, convertErrors(bindingResult));
    }

    private static Map<String, Object> convertErrors(BindingResult bindingResult) {
        Map<String, Object> details = new HashMap<>();


        Map<String, List<String>> fieldErrors = bindingResult.getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        fieldError -> fieldError.getField(),
                        Collectors.mapping(
                                fieldError -> fieldError.getDefaultMessage(),
                                Collectors.toList()
                        )
                ));


        List<String> globalErrors = bindingResult.getGlobalErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        details.put("fieldErrors", fieldErrors);
        details.put("globalErrors", globalErrors);

        return details;
    }
}