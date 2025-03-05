package org.example.prm392_groupprojectbe.services;

import org.example.prm392_groupprojectbe.dtos.accounts.AccountResponseDTO;
import org.example.prm392_groupprojectbe.dtos.auth.requests.*;
import org.example.prm392_groupprojectbe.dtos.auth.response.AuthResponseDTO;
import org.example.prm392_groupprojectbe.entities.Account;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthService extends UserDetailsService {

    Account register(RegisterRequestDTO requestDTO);

    AuthResponseDTO login(LoginRequestDTO requestDTO);

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    void sendOtp(SendOtpRequestDTO requestDTO);

    void verifyOtp(VerifyOtpRequestDTO request);

    AccountResponseDTO updateProfile(UpdateProfileRequestDTO requestDTO);
}
