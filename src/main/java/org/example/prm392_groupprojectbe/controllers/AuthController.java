package org.example.prm392_groupprojectbe.controllers;

import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.dtos.BaseResponseDTO;
import org.example.prm392_groupprojectbe.dtos.accounts.AccountResponseDTO;
import org.example.prm392_groupprojectbe.dtos.auth.requests.*;
import org.example.prm392_groupprojectbe.dtos.auth.response.AuthResponseDTO;
import org.example.prm392_groupprojectbe.services.AuthService;
import org.example.prm392_groupprojectbe.utils.JwtUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("**")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService accountService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponseDTO> register(@RequestBody RegisterRequestDTO requestDTO) {
        accountService.register(requestDTO);
        return ResponseEntity.ok(
                BaseResponseDTO.builder()
                        .message("Register successfully, please check your email to confirm verification")
                        .data(null)
                        .success(true)
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        AuthResponseDTO responseDTO = accountService.login(loginRequest);
        return ResponseEntity.ok(
                BaseResponseDTO.builder()
                        .message("Login successfully")
                        .data(responseDTO)
                        .success(true)
                        .build()
        );
    }

    @PutMapping("/update-profile")
    public ResponseEntity<BaseResponseDTO> updateProfile(@RequestBody UpdateProfileRequestDTO requestDTO) {
        AccountResponseDTO updatedAccount = accountService.updateProfile(requestDTO);
        return ResponseEntity.ok(
                BaseResponseDTO.builder()
                        .message("Profile updated successfully.")
                        .data(updatedAccount)
                        .success(true)
                        .build()
        );
    }

    @PostMapping("/send-verify-email")
    public ResponseEntity<BaseResponseDTO> sendVerifyEmail(@RequestBody SendOtpRequestDTO requestDTO) {
        accountService.sendOtp(requestDTO);
        return ResponseEntity.ok(
                BaseResponseDTO.builder()
                        .message("OTP sent to your email.")
                        .data(null)
                        .success(true)
                        .build()
        );
    }

    @PostMapping("/verify-email")
    public ResponseEntity<BaseResponseDTO> verifyEmail(@RequestBody VerifyOtpRequestDTO requestDTO) {
        accountService.verifyOtp(requestDTO);
        return ResponseEntity.ok(
                BaseResponseDTO.builder()
                        .message("Verify account successfully")
                        .data(null)
                        .success(true)
                        .build()
        );
    }
}
