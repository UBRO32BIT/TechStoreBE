package org.example.prm392_groupprojectbe.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.dtos.BaseResponseDTO;
import org.example.prm392_groupprojectbe.dtos.orders.request.CreateOrderRequestDTO;
import org.example.prm392_groupprojectbe.dtos.orders.response.OrderResponseDTO;
import org.example.prm392_groupprojectbe.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<BaseResponseDTO> createOrder(@Valid @RequestBody CreateOrderRequestDTO requestDTO) {
        OrderResponseDTO responseDTO =  orderService.createOrder(requestDTO);
        return ResponseEntity.ok(
                BaseResponseDTO.builder()
                        .message("Create order successfully")
                        .success(true)
                        .data(responseDTO)
                        .build()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<BaseResponseDTO> getOrdersByCurrentUser() {
        List<OrderResponseDTO> responseDTO = orderService.getOrdersByCurrentUser();
        return ResponseEntity.ok(
                BaseResponseDTO.builder()
                        .message("Get orders by current user")
                        .success(true)
                        .data(responseDTO)
                        .build()
        );
    }
}
