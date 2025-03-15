package org.example.prm392_groupprojectbe.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.constants.ZaloPayConstants;
import org.example.prm392_groupprojectbe.dtos.payment.response.PaymentResponseDTO;
import org.example.prm392_groupprojectbe.dtos.zalopay.*;
import org.example.prm392_groupprojectbe.entities.Account;
import org.example.prm392_groupprojectbe.entities.Order;
import org.example.prm392_groupprojectbe.entities.OrderDetail;
import org.example.prm392_groupprojectbe.entities.Payment;
import org.example.prm392_groupprojectbe.enums.PaymentMethod;
import org.example.prm392_groupprojectbe.enums.PaymentStatus;
import org.example.prm392_groupprojectbe.exceptions.AppException;
import org.example.prm392_groupprojectbe.exceptions.ErrorCode;
import org.example.prm392_groupprojectbe.proxy.ZaloPayProxy;
import org.example.prm392_groupprojectbe.repositories.PaymentRepository;
import org.example.prm392_groupprojectbe.services.OrderService;
import org.example.prm392_groupprojectbe.services.PaymentService;
import org.example.prm392_groupprojectbe.utils.DateUtil;
import org.example.prm392_groupprojectbe.utils.HMACUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private OrderService orderService;
    private final ZaloPayProxy zaloPayProxy;

    @Lazy
    @Autowired
    private void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    @Transactional
    public PaymentResponseDTO createPayment(
            Order order,
            List<OrderDetail> orderDetail,
            PaymentMethod paymentMethod
    ) {
        Payment payment = Payment.builder()
                .paymentMethod(paymentMethod)
                .paymentStatus(PaymentStatus.PENDING)
                .amount(order.getTotalPrice())
                .order(order)
                .user(order.getUser())
                .build();


        switch (paymentMethod) {
            case ZALOPAY -> {
                ZaloPayOrderResponseDTO zalopay = this.createPaymentUrl(order.getUser(), order, orderDetail);
                payment.setTransactionId(zalopay.getOrderToken());
                Payment savedPayment = paymentRepository.save(payment);
                return PaymentResponseDTO.builder()
                        .id(savedPayment.getId())
                        .method(paymentMethod)
                        .status(savedPayment.getPaymentStatus())
                        .amount(savedPayment.getAmount())
                        .isPaid(savedPayment.isPaid())
                        .isRefunded(savedPayment.isRefunded())
                        .zalopay(zalopay)
                        .transactionId(savedPayment.getTransactionId())
                        .build();
            }
            case CASH -> {
                Payment savedPayment = paymentRepository.save(payment);
                return PaymentResponseDTO.builder()
                        .id(savedPayment.getId())
                        .method(paymentMethod)
                        .status(savedPayment.getPaymentStatus())
                        .amount(savedPayment.getAmount())
                        .isPaid(savedPayment.isPaid())
                        .isRefunded(savedPayment.isRefunded())
                        .transactionId(savedPayment.getTransactionId())
                        .build();
            }
            default -> throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ZaloPayOrderResponseDTO createPaymentUrl(Account account, Order order, List<OrderDetail> orderDetails) {
        try {
            ZaloPayCreateOrderRequestDTO requestDTO = ZaloPayCreateOrderRequestDTO.builder()
                    .appId(ZaloPayConstants.APP_ID)
                    .appTransId(DateUtil.getCurrentTimeString("yyMMdd") + "_" + new Date().getTime())
                    .appTime(System.currentTimeMillis())
                    .appUser(String.valueOf(account.getId()))
                    .amount(order.getTotalPrice().longValue())
                    .item("[{}]")
                    .bankCode(ZaloPayConstants.BANK_CODE)
                    .embedData("{}")
                    .description("Payment for order #" + order.getId())
                    .callbackUrl(ZaloPayConstants.ZALOPAY_CALLBACK_URL)
                    .build();

            String combinedData = requestDTO.getAppId() + "|"
                    + requestDTO.getAppTransId() + "|"
                    + requestDTO.getAppUser() + "|"
                    + requestDTO.getAmount() + "|"
                    + requestDTO.getAppTime() +"|"
                    + requestDTO.getEmbedData() +"|"
                    + requestDTO.getItem();

            requestDTO.setMac(HMACUtil.HMacHexStringEncode(
                    HMACUtil.HMACSHA256,
                    ZaloPayConstants.KEY1,
                    combinedData
            ));
            ZaloPayOrderResponseDTO paymentDetail = zaloPayProxy.createOrder(requestDTO);
            if (paymentDetail.getReturnCode() != 1) {
                throw new RuntimeException(paymentDetail.getSubReturnMessage());
            }
            paymentDetail.setAppId(requestDTO.getAppId());

            return paymentDetail;
        }
        catch (FeignException ex) {
            throw ex;
        }
    }

    @Override
    public ZaloPayCallbackResponseDTO handleCallback(ZaloPayCallbackDTO callbackDTO) {
        ZaloPayCallbackResponseDTO responseData = ZaloPayCallbackResponseDTO.builder().build();
        try {
            //VALIDATE SIGNATURE
            String signature = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, ZaloPayConstants.KEY2, callbackDTO.getData());
            if (signature.equals(callbackDTO.getMac())) {
                throw new AppException(ErrorCode.INVALID_SIGNATURE);
            }

            //GET DATA FROM ZALOPAY
            ZaloPayTransactionDTO data = objectMapper.readValue(callbackDTO.getData(), ZaloPayTransactionDTO.class);
            Payment payment = paymentRepository.findByTransactionId(data.getAppTransId())
                    .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

            //SET PAYMENT STATUS TO PAID
            payment.setExtTransactionId(String.valueOf(data.getZpTransId()));
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setPaid(true);
            paymentRepository.save(payment);

            //UPDATE ORDER STATUS
            orderService.handlePaymentCompletion(payment.getOrder());
            responseData.setReturnCode(1);
            responseData.setReturnMessage("Success");
        }
        catch (AppException e) {
            responseData.setReturnCode(-1);
            responseData.setReturnMessage(e.getErrorCode().getMessage());
        }
        catch (Exception e) {
            responseData.setReturnCode(0);
            responseData.setReturnMessage(e.getMessage());
        }
        return responseData;
    }
}
