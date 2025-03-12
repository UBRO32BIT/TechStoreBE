package org.example.prm392_groupprojectbe.dtos.zalopay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZaloPayCallbackDTO {
    private String data;
    private String mac;
    private Integer type;
}
