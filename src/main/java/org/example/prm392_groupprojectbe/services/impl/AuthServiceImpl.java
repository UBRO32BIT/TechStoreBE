package org.example.prm392_groupprojectbe.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.dtos.auth.requests.RegisterRequestDTO;
import org.example.prm392_groupprojectbe.entities.Account;
import org.example.prm392_groupprojectbe.enums.AccountRoleEnum;
import org.example.prm392_groupprojectbe.enums.AccountStatusEnum;
import org.example.prm392_groupprojectbe.repositories.AccountRepository;
import org.example.prm392_groupprojectbe.services.AuthService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountRepository accountRepository;
    @Lazy
    private final PasswordEncoder passwordEncoder;

    @Override
    public Account register(RegisterRequestDTO requestDTO) {
        Account account = Account.builder()
                .name(requestDTO.getName())
                .status(AccountStatusEnum.UNVERIFIED)
                .role(AccountRoleEnum.USER)
                .phoneNumber(requestDTO.getPhoneNumber())
                .gender(requestDTO.getGender())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .build();
        return accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
