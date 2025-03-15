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
    INVALID_SIGNATURE(7, "Invalid signature", HttpStatus.BAD_REQUEST),
    WRONG_CREDENTIALS(101, "Wrong credentials, please try again", HttpStatus.UNAUTHORIZED),
    OTP_REQUEST_TOO_FREQUENT(103, "OTP cooldown exceeded, please wait a few seconds before requesting again", HttpStatus.BAD_REQUEST),
    INVALID_OTP(102, "Invalid OTP", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(103, "Email already exists", HttpStatus.CONFLICT),
    ACCOUNT_BANNED(104, "The current account is banned", HttpStatus.FORBIDDEN),
    ACCOUNT_DISABLED(105, "The current account is disabled", HttpStatus.FORBIDDEN),
    ACCOUNT_NOT_VERIFIED(106, "The current account is not verified", HttpStatus.FORBIDDEN),
    ACCOUNT_NOT_FOUND(107, "Account not found", HttpStatus.NOT_FOUND),
    OLD_PASSWORD_INCORRECT(108, "Old password is incorrect", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(200, "User not found", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND(300, "Order not found", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(400, "Category not found", HttpStatus.NOT_FOUND),
    PAYMENT_FAILED(600, "Payment failed from external provider", HttpStatus.BAD_REQUEST),
    PAYMENT_NOT_FOUND(601, "Payment not found", HttpStatus.NOT_FOUND),
    INVALID_STOCK(602, "Invalid stock", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}

