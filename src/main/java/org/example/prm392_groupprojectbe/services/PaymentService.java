package org.example.prm392_groupprojectbe.services;

import org.example.prm392_groupprojectbe.dtos.payment.response.PaymentResponseDTO;
import org.example.prm392_groupprojectbe.dtos.zalopay.ZaloPayCallbackDTO;
import org.example.prm392_groupprojectbe.dtos.zalopay.ZaloPayCallbackResponseDTO;
import org.example.prm392_groupprojectbe.dtos.zalopay.ZaloPayOrderResponseDTO;
import org.example.prm392_groupprojectbe.entities.Account;
import org.example.prm392_groupprojectbe.entities.Order;
import org.example.prm392_groupprojectbe.entities.OrderDetail;
import org.example.prm392_groupprojectbe.enums.PaymentMethod;

import java.util.List;

public interface PaymentService {
    PaymentResponseDTO createPayment(Order order, List<OrderDetail> orderDetails, PaymentMethod paymentMethod);

    ZaloPayOrderResponseDTO createPaymentUrl(Account account, Order order, List<OrderDetail> orderDetails);

    ZaloPayCallbackResponseDTO handleCallback(ZaloPayCallbackDTO callbackDTO);
}
