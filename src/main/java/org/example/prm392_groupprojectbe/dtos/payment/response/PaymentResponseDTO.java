package org.example.prm392_groupprojectbe.dtos.payment.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.prm392_groupprojectbe.dtos.zalopay.ZaloPayOrderResponseDTO;
import org.example.prm392_groupprojectbe.enums.PaymentMethod;
import org.example.prm392_groupprojectbe.enums.PaymentStatus;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private Long id;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private ZaloPayOrderResponseDTO zalopay;
    @JsonProperty("isPaid")
    private Boolean isPaid;
    @JsonProperty("isRefunded")
    private Boolean isRefunded;
    private String transactionId;
}
