package org.example.prm392_groupprojectbe.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(1, "An unexpected error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST_BODY(2, "Invalid request body", HttpStatus.BAD_REQUEST),
    TOO_MANY_REQUESTS(3, "Too many requests! Please try again later.", HttpStatus.BAD_GATEWAY),
    RESOURCE_NOT_FOUND(4, "Resource not found", HttpStatus.NOT_FOUND),
    INVALID_DATE_FORMAT(5, "Invalid date format", HttpStatus.BAD_REQUEST),
    INVALID_SORT_FIELD(6, "Invalid sort field", HttpStatus.BAD_REQUEST),
    WRONG_CREDENTIALS(101, "Wrong credentials, please try again", HttpStatus.UNAUTHORIZED),
    OTP_REQUEST_TOO_FREQUENT(103, "OTP cooldown exceeded, please wait a few seconds before requesting again", HttpStatus.BAD_REQUEST),
    INVALID_OTP(102, "Invalid OTP", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(103, "Email already exists", HttpStatus.CONFLICT),
    ACCOUNT_BANNED(104, "The current account is banned", HttpStatus.FORBIDDEN),
    ACCOUNT_LOCKED(105, "The current account is locked", HttpStatus.FORBIDDEN),
    ACCOUNT_NOT_VERIFIED(106, "The current account is not verified", HttpStatus.FORBIDDEN),
    ACCOUNT_NOT_FOUND(106, "Account not found", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(200, "User not found", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND(300, "Order not found", HttpStatus.NOT_FOUND),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}

