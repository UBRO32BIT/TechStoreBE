package org.example.prm392_groupprojectbe.services;

import org.example.prm392_groupprojectbe.dtos.accounts.AccountResponseDTO;
import org.example.prm392_groupprojectbe.dtos.auth.requests.RegisterRequestDTO;
import org.example.prm392_groupprojectbe.dtos.auth.requests.UpdateProfileRequestDTO;
import org.example.prm392_groupprojectbe.entities.Account;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthService extends UserDetailsService {

    Account register(RegisterRequestDTO requestDTO);

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    AccountResponseDTO updateProfile(UpdateProfileRequestDTO requestDTO);
}
