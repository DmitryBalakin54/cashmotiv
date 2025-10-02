package ru.cashmotiv.cashmotiv.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NOTFOUND_EMAIL_TOKEN(4001, "error.notfound_email_token.4001"),
    NOTFOUND_USER(4002, "error.notfound_user.4002"),
    NOTFOUND_EMAIL(4003, "error.notfound_email.4003"),
    NOTFOUND_PASSWORD_TOKEN(4004, "error.notfound_password_token.4004"),
    NOTFOUND_PROMISE(4005, "error.notfound_promise.4005"),


    VALIDATION_FAILED(2000, "error.validation.failed.2000"),
    FIELD_REQUIRED(2001, "error.field.required.2001"),
    INVALID_EMAIL(2002, "error.field.email.2002"),
    CANT_SAVE_USER(2003, "error.cant_save_user.2003"),
    CANT_SAVE_EMAIL(2004, "error.cant_save_email.2004"),
    CANT_SAVE_MAILING_EMAIL(2005, "error.cant_save_mailing_email.2005"),
    CANT_SAVE_PROMISE(2006, "error.cant_save_promise.2006"),
    CANT_SAVE_PASSWORD_TOKEN(2007, "error.cant_save_password_token.2007"),
    CANT_SAVE_EMAIL_TOKEN(2008, "error.cant_save_email_token.2008"),


    DUPLICATE_EMAIL(3001, "error.duplicate_email.3001"),
    INCORRECT_EMAIL(3002, "error.incorrect_email.3002"),
    EMAIL_IS_NOT_VERIFIED(3003, "error.email_is_not_verified.3003"),
    EMAIL_IS_ALREADY_MAIN(3004, "error.email_is_already_main.3004"),
    CANT_CREATE_JWT(3005, "error.cant_create_jwt.3005"),
    CANT_FIND_JWT(3006, "error.cant_find_jwt.3006"),
    DUPLICATE_USER(3007, "error.duplicate_user.3007"),
    LOGIN_NOT_EXIST(3008, "error.login_not_exist.3008"),
    INCORRECT_PASSWORD(3009, "error.incorrect_password.3009"),
    INCORRECT_EMAIL_ID(3010, "error.incorrect_email_id.3010"),
    CANT_DELETE_MAIN_EMAIL(3011, "error.cant_delete_main_email.3011"),
    INCORRECT_MAILING_EMAIL_TOKEN(3012, "error.incorrect_mailing_email_token.3012"),
    EMAIL_IS_ALREADY_VERIFIED(3013, "error.email_is_already_verified.3013"),
    INCORRECT_DATE_FORMAT(3014, "error.incorrect_date_format.3014"),
    INCORRECT_DATE_IN_PAST(3015, "error.incorrect_date_in_past.3015"),
    INCORRECT_DATE_PROMISE_BOUND(3016, "error.incorrect_date_promise_bound.3016"),
    INCORRECT_PROMISE_ID(3017, "error.incorrect_promise_id.3017"),
    PROMISE_IS_ALREADY_CLOSE(3018, "error.promise_is_already_close.3018"),
    INCORRECT_PROMISE_TYPE(3019, "error.incorrect_promise_type.3019"),
    PROMISE_NOT_IN_PROGRESS(3020, "error.promise_not_in_progress.3020"),
    TOO_LATE_CLOSE_PROMISE(3021, "error.too_late_close_promise.3021"),
    INCORRECT_NOTIFICATION_DATE_AFTER_EXPIRE_DATE(3022, "error.incorrect_notification_date_after_expire_date.3022"),
    INCORRECT_NOTIFICATION_DATE_TOO_LITTLE_TIME(3023, "error.incorrect_notification_date_too_little_time.3023"),
    INCORRECT_NOTIFICATION_DATE_BEFORE_CREATE(3024,  "error.incorrect_notification_date_before_create.3024"),
    INCORRECT_IMAGE_TYPE(3025, "error.incorrect_image_type.3025"),
    INCORRECT_IMAGE_IS_NULL(3026, "error.incorrect_image_is_null.3026"),
    INCORRECT_IMAGE_MIME_TYPE(3027, "error.incorrect_image_mimetype.3027"),


    INTERNAL_ERROR(1000, "error.system_internal.1000"),
    FILES_ERROR(1001, "error.files_error.1001"),
    ACCESS_DENIED(1002, "error.access_denied.1002");

    private final int code;
    private final String defaultMessage;

    ErrorCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}