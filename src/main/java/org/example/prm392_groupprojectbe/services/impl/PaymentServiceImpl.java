package org.example.prm392_groupprojectbe.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.constants.ZaloPayConstants;
import org.example.prm392_groupprojectbe.dtos.zalopay.ZaloPayCallbackDTO;
import org.example.prm392_groupprojectbe.dtos.zalopay.ZaloPayTransactionDTO;
import org.example.prm392_groupprojectbe.entities.Payment;
import org.example.prm392_groupprojectbe.enums.PaymentStatus;
import org.example.prm392_groupprojectbe.exceptions.AppException;
import org.example.prm392_groupprojectbe.exceptions.ErrorCode;
import org.example.prm392_groupprojectbe.repositories.PaymentRepository;
import org.example.prm392_groupprojectbe.utils.HMACUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl {
    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    public void handleCallback(ZaloPayCallbackDTO callbackDTO) {
        try {
            String signatrue = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, ZaloPayConstants.KEY2, callbackDTO.getData());
            if (signatrue.equals(callbackDTO.getMac())) {
                throw new AppException(ErrorCode.INVALID_SIGNATURE);
            }
            ZaloPayTransactionDTO data = objectMapper.readValue(callbackDTO.getData(), ZaloPayTransactionDTO.class);
            Payment payment = paymentRepository.findByTransactionId(data.getAppTransId())
                    .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
            payment.setExtTransactionId(String.valueOf(data.getZpTransId()));
            payment.setPaymentStatus(PaymentStatus.PAID);
            paymentRepository.save(payment);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
