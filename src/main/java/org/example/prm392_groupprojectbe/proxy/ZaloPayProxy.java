package org.example.prm392_groupprojectbe.proxy;

import feign.FeignException;
import org.example.prm392_groupprojectbe.constants.ZaloPayConstants;
import org.example.prm392_groupprojectbe.dtos.zalopay.ZaloPayCreateOrderRequestDTO;
import org.example.prm392_groupprojectbe.dtos.zalopay.ZaloPayOrderResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "zalopay-client", url = ZaloPayConstants.ZALOPAY_ENDPOINTS)
public interface ZaloPayProxy {
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Retryable(value = FeignException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    ZaloPayOrderResponseDTO createOrder(@RequestBody ZaloPayCreateOrderRequestDTO requestBody);
}
