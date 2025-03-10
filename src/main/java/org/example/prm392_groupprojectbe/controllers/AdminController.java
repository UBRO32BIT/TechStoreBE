package org.example.prm392_groupprojectbe.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.dtos.accounts.AccountResponseDTO;
import org.example.prm392_groupprojectbe.dtos.auth.requests.UpdateProfileRequestDTO;
import org.example.prm392_groupprojectbe.dtos.orders.request.GetOrdersRequestDTO;
import org.example.prm392_groupprojectbe.dtos.orders.response.OrderResponseDTO;
import org.example.prm392_groupprojectbe.dtos.product.request.CreateProductRequestDTO;
import org.example.prm392_groupprojectbe.dtos.product.request.UpdateProductRequestDTO;
import org.example.prm392_groupprojectbe.entities.Product;
import org.example.prm392_groupprojectbe.enums.AccountRoleEnum;
import org.example.prm392_groupprojectbe.enums.AccountStatusEnum;
import org.example.prm392_groupprojectbe.enums.OrderStatus;
import org.example.prm392_groupprojectbe.services.AccountService;
import org.example.prm392_groupprojectbe.services.OrderService;
import org.example.prm392_groupprojectbe.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin("**")
@RequiredArgsConstructor
public class AdminController {
    private final AccountService accountService;
    private final OrderService orderService;
    private final ProductService productService;

//    @GetMapping("/accounts")
//    public ResponseEntity<Page<AccountResponseDTO>> getUsers(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(required = false) String search,
//            @RequestParam(required = false) AccountRoleEnum role,
//            @RequestParam(required = false) AccountStatusEnum status) {
//        Page<AccountResponseDTO> users = accountService.getUsers(page, size, search, role, status);
//        return ResponseEntity.ok(users);
//    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(accountService.getAllUsers());
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getUserById(id));
    }

    @PatchMapping("/accounts/{id}")
    public ResponseEntity<AccountResponseDTO> updateUser(@PathVariable Long id, @RequestBody UpdateProfileRequestDTO requestDTO) {
        return ResponseEntity.ok(accountService.updateUser(id, requestDTO));
    }

    @PutMapping("/accounts/{id}/ban")
    public ResponseEntity<Void> banUser(@PathVariable Long id) {
        accountService.banUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/accounts/{id}/unban")
    public ResponseEntity<Void> unbanUser(@PathVariable Long id) {
        accountService.unbanUser(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/orders")
//    public ResponseEntity<Page<OrderResponseDTO>> getOrders(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "createdAt") String sortBy,
//            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
//            @RequestParam(required = false) LocalDateTime startDate,
//            @RequestParam(required = false) LocalDateTime endDate,
//            @RequestParam(required = false) OrderStatus status) {
//        GetOrdersRequestDTO requestDTO = GetOrdersRequestDTO.builder()
//                .startDate(startDate)
//                .endDate(endDate)
//                .pageNumber(page)
//                .pageSize(size)
//                .sortBy(sortBy)
//                .status(status)
//                .direction(direction)
//                .build();
//        Page<OrderResponseDTO> orders = orderService.getAllOrders(requestDTO);
//        return ResponseEntity.ok(orders);
//    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getOrders());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody CreateProductRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(dto));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequestDTO dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}