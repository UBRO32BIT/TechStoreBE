package org.example.prm392_groupprojectbe.dtos.zalopay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ZaloPayCallbackResponseDTO {
    @JsonProperty("return_code")
    private Integer returnCode;
    @JsonProperty("return_message")
    private String returnMessage;
}
