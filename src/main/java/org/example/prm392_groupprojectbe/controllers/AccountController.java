package org.example.prm392_groupprojectbe.controllers;

import jakarta.validation.Valid;
import org.example.prm392_groupprojectbe.dtos.auth.requests.ChangePasswordRequestDTO;
import org.example.prm392_groupprojectbe.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequestDTO request) {
        accountService.changePassword(request);
        return ResponseEntity.ok("Password changed successfully");
    }
}