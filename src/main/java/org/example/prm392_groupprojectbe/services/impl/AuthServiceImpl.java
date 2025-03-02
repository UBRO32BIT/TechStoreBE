package org.example.prm392_groupprojectbe.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.dtos.accounts.AccountResponseDTO;
import org.example.prm392_groupprojectbe.dtos.auth.requests.RegisterRequestDTO;
import org.example.prm392_groupprojectbe.dtos.auth.requests.UpdateProfileRequestDTO;
import org.example.prm392_groupprojectbe.entities.Account;
import org.example.prm392_groupprojectbe.enums.AccountRoleEnum;
import org.example.prm392_groupprojectbe.enums.AccountStatusEnum;
import org.example.prm392_groupprojectbe.exceptions.AppException;
import org.example.prm392_groupprojectbe.exceptions.ErrorCode;
import org.example.prm392_groupprojectbe.mappers.AccountMapper;
import org.example.prm392_groupprojectbe.repositories.AccountRepository;
import org.example.prm392_groupprojectbe.services.AuthService;
import org.example.prm392_groupprojectbe.utils.AccountUtils;
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
    private final AccountMapper accountMapper;

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

    @Override
    public AccountResponseDTO updateProfile(UpdateProfileRequestDTO requestDTO) {
        Account currentAccount = AccountUtils.getCurrentAccount();

        if (currentAccount == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        currentAccount.setName(requestDTO.getName());
        currentAccount.setPhoneNumber(requestDTO.getPhoneNumber());
        currentAccount.setGender(requestDTO.getGender());

        if (requestDTO.getAvatar() != null) {
            currentAccount.setAvatar(requestDTO.getAvatar());
        }

        Account result = accountRepository.save(currentAccount);
        return accountMapper.toDTO(result);
    }

}
