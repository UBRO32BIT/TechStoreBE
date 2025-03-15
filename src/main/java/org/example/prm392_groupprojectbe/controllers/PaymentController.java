package org.example.prm392_groupprojectbe.controllers;

import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.dtos.zalopay.ZaloPayCallbackDTO;
import org.example.prm392_groupprojectbe.dtos.zalopay.ZaloPayCallbackResponseDTO;
import org.example.prm392_groupprojectbe.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/zalopay-callback-handler")
    public ResponseEntity<ZaloPayCallbackResponseDTO> callback(@RequestBody ZaloPayCallbackDTO request) {
        return ResponseEntity.ok(
                paymentService.handleCallback(request)
        );
    }
}
