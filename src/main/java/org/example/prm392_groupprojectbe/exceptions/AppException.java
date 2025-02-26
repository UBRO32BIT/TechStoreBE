package org.example.prm392_groupprojectbe.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;
}
