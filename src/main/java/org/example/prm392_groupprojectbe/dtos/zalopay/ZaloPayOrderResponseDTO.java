package org.example.prm392_groupprojectbe.dtos.zalopay;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZaloPayOrderResponseDTO {
    @JsonAlias("return_code")
    private int returnCode;

    @JsonAlias("return_message")
    private String returnMessage;

    @JsonAlias("sub_return_code")
    private int subReturnCode;

    @JsonAlias("sub_return_message")
    private String subReturnMessage;

    @JsonAlias("order_url")
    private String orderUrl;

    @JsonAlias("zp_trans_token")
    private String zpTransToken;

    @JsonAlias("order_token")
    private String orderToken;

    @JsonAlias("qr_code")
    private String qrCode;

    private Integer appId;
}
