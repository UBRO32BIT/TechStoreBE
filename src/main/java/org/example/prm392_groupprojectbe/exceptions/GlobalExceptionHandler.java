package org.example.prm392_groupprojectbe.exceptions;

import org.example.prm392_groupprojectbe.dtos.BaseResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponseDTO.builder()
                        .message(ErrorCode.INVALID_REQUEST_BODY.getMessage())
                        .errorCode(ErrorCode.INVALID_REQUEST_BODY.getCode())
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
