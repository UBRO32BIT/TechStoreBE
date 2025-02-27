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
    WRONG_CREDENTIALS(101, "Wrong credentials, please try again", HttpStatus.UNAUTHORIZED),
    RESOURCE_NOT_FOUND(102, "Resource not found", HttpStatus.NOT_FOUND),
    INVALID_DATE_FORMAT(103, "Invalid date format", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(200, "User not found", HttpStatus.NOT_FOUND),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}

