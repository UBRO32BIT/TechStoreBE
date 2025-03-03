package org.example.prm392_groupprojectbe.controllers;

import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.dtos.BaseResponseDTO;
import org.example.prm392_groupprojectbe.dtos.accounts.AccountResponseDTO;
import org.example.prm392_groupprojectbe.dtos.auth.requests.LoginRequestDTO;
import org.example.prm392_groupprojectbe.dtos.auth.requests.RegisterRequestDTO;
import org.example.prm392_groupprojectbe.dtos.auth.requests.UpdateProfileRequestDTO;
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

    @Lazy
    private final AuthService accountService;
    @Lazy
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<BaseResponseDTO> register(@RequestBody RegisterRequestDTO requestDTO) {
        accountService.register(requestDTO);
        return ResponseEntity.ok(
                BaseResponseDTO.builder()
                        .message("Register successfully.")
                        .data(null)
                        .success(true)
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        UserDetails userDetails = accountService.loadUserByUsername(loginRequest.getEmail());
        String jwt = jwtUtil.generateToken(userDetails);
        AuthResponseDTO responseDTO = AuthResponseDTO.builder()
                .token(jwt)
                .build();

        return ResponseEntity.ok(
                BaseResponseDTO.builder()
                        .data(responseDTO)
                        .success(true)
                        .build()
        );
    }

    @PatchMapping("/update-profile")
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
}
