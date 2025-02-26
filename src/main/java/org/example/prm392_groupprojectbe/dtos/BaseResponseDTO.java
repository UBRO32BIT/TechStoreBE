package org.example.prm392_groupprojectbe.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseResponseDTO {
    private String message;
    private Boolean success;
    private Integer errorCode;
    private Object data;
}
