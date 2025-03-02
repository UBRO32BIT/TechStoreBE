package org.example.prm392_groupprojectbe.controllers;

import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.dtos.accounts.AccountResponseDTO;
import org.example.prm392_groupprojectbe.enums.AccountRoleEnum;
import org.example.prm392_groupprojectbe.enums.AccountStatusEnum;
import org.example.prm392_groupprojectbe.services.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin("**")
@RequiredArgsConstructor
public class AdminController {
    private final AccountService accountService;

    @GetMapping("/accounts")
    public ResponseEntity<Page<AccountResponseDTO>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) AccountRoleEnum role,
            @RequestParam(required = false) AccountStatusEnum status) {
        Page<AccountResponseDTO> users = accountService.getUsers(page, size, search, role, status);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getUserById(id));
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
}