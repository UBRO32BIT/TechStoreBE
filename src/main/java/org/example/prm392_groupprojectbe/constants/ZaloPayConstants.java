package org.example.prm392_groupprojectbe.constants;

public class ZaloPayConstants {
    private ZaloPayConstants() {}

    public static final int APP_ID = 2553;
    public static final String KEY1 = "PcY4iZIKFCIdgZvA6ueMcMHHUbRLYjPL";
    public static final String KEY2 = "kLtgPl8HHhfvMuDHPwKfgfsY4Ydm9eIz";
    public static final String BANK_CODE = "zalopayapp";
    public static final String ZALOPAY_ENDPOINTS = "https://sb-openapi.zalopay.vn/v2";
    public static final String ZALOPAY_CALLBACK_URL = "http://techstore.32mine.net:8080/payment/zalopay-callback";
    public static final String ZALOPAY_ORDER_CREATE_ENDPOINT
            = "/create";

    public static final String ORDER_STATUS_ENDPOINT
            = "https://sandbox.zalopay.com.vn/v001/tpe/getstatusbyapptransid";

    public static final String REFUND_PAYMENT_ENDPOINT
            = "https://sb-openapi.zalopay.vn/v2/refund";

    public static final String REFUND_STATUS_PAYMENT_ENDPOINT
            = "https://sb-openapi.zalopay.vn/v2/query_refund";
}
