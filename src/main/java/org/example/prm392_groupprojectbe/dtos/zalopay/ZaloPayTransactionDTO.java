package org.example.prm392_groupprojectbe.dtos.zalopay;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZaloPayTransactionDTO {
    @JsonAlias("app_id")
    private int appId;

    @JsonAlias("app_trans_id")
    private String appTransId;

    @JsonAlias("app_time")
    private long appTime;

    @JsonAlias("app_user")
    private String appUser;

    private long amount;

    @JsonAlias("embed_data")
    private String embedData;

    private List<ZaloPayItem> item;

    @JsonAlias("zp_trans_id")
    private long zpTransId;

    @JsonAlias("server_time")
    private long serverTime;

    private int channel;

    @JsonAlias("merchant_user_id")
    private String merchantUserId;

    @JsonAlias("user_fee_amount")
    private long userFeeAmount;

    @JsonAlias("discount_amount")
    private long discountAmount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    class ZaloPayItem {

        @JsonAlias("itemid")
        private String itemId;

        @JsonAlias("itename")
        private String itemName;

        @JsonAlias("itemprice")
        private long itemPrice;

        @JsonAlias("itemquantity")
        private int itemQuantity;
    }
}
