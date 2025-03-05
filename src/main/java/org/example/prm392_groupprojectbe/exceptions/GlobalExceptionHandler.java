package org.example.prm392_groupprojectbe.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.example.prm392_groupprojectbe.dtos.BaseResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final Pattern ENUM_MSG = Pattern.compile("values accepted for Enum class: \\[(.*?)\\]");

    @ExceptionHandler(AppException.class)
    public ResponseEntity<BaseResponseDTO> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity.status(errorCode.getHttpStatus()).body(
                BaseResponseDTO.builder()
                        .message(errorCode.getMessage())
                        .errorCode(errorCode.getCode())
                        .success(false)
                        .build()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logger.error("Invalid request body: {}", ex.getMessage());

        //ENUM PARSING ERROR
        if (ex.getCause() instanceof InvalidFormatException) {
            Matcher match = ENUM_MSG.matcher(ex.getCause().getMessage());
            if (match.find()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        BaseResponseDTO.builder()
                                .message("Invalid value. Allowed values: " + match.group(1))
                                .errorCode(ErrorCode.INVALID_REQUEST_BODY.getCode())
                                .success(false)
                                .build()
                );
            }
        }

        //OTHER
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponseDTO.builder()
                        .message(ErrorCode.INVALID_REQUEST_BODY.getMessage())
                        .errorCode(ErrorCode.INVALID_REQUEST_BODY.getCode())
                        .success(false)
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        logger.error("Validation error: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponseDTO.builder()
                        .message("Validation failed")
                        .errorCode(ErrorCode.INVALID_REQUEST_BODY.getCode())
                        .success(false)
                        .data(errors) // Sends validation errors in response
                        .build()
        );
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<BaseResponseDTO> handleDateTimeParseException(DateTimeParseException ex) {
        logger.error("Date parse exception: {}", ex.getMessage());

        return ResponseEntity.status(ErrorCode.INVALID_DATE_FORMAT.getHttpStatus()).body(
                BaseResponseDTO.builder()
                        .message("Invalid date format. Please use ISO format for dates.")
                        .errorCode(ErrorCode.INVALID_DATE_FORMAT.getCode())
                        .success(false)
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseDTO> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                BaseResponseDTO.builder()
                        .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                        .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                        .success(false)
                        .build()
        );
    }
}
