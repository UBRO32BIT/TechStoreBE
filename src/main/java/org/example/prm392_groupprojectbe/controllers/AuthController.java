package org.example.prm392_groupprojectbe.controllers;

import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.dtos.BaseResponseDTO;
import org.example.prm392_groupprojectbe.dtos.auth.requests.LoginRequestDTO;
import org.example.prm392_groupprojectbe.dtos.auth.requests.RegisterRequestDTO;
import org.example.prm392_groupprojectbe.dtos.auth.response.AuthResponseDTO;
import org.example.prm392_groupprojectbe.entities.Account;
import org.example.prm392_groupprojectbe.services.AuthService;
import org.example.prm392_groupprojectbe.utils.JwtUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
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
}
