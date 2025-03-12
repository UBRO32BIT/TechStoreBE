package org.example.prm392_groupprojectbe.dtos.zalopay;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZaloPayCreateOrderRequestDTO {
    @JsonProperty("app_id")
    private Integer appId;

    @JsonProperty("app_user")
    private String appUser;

    @JsonProperty("app_trans_id")
    private String appTransId;

    @JsonProperty("app_time")
    private long appTime;

    @JsonProperty("expire_duration_seconds")
    private Long expireDurationSeconds;

    @JsonProperty("amount")
    private long amount;

    @JsonProperty("item")
    private String item;

    @JsonProperty("description")
    private String description;

    @JsonProperty("embed_data")
    private String embedData;

    @JsonProperty("bank_code")
    private String bankCode;

    @JsonProperty("mac")
    private String mac;

    @JsonProperty("callback_url")
    private String callbackUrl;

    @JsonProperty("device_info")
    private Map<String, Object> deviceInfo;

    @JsonProperty("sub_app_id")
    private String subAppId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        @JsonProperty("itemid")
        private String itemId;

        @JsonProperty("itename")
        private String itemName;

        @JsonProperty("itemprice")
        private long itemPrice;

        @JsonProperty("itemquantity")
        private int itemQuantity;
    }
}
