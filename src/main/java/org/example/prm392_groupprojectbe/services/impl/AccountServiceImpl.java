package org.example.prm392_groupprojectbe.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.dtos.accounts.AccountResponseDTO;
import org.example.prm392_groupprojectbe.dtos.auth.requests.ChangePasswordRequestDTO;
import org.example.prm392_groupprojectbe.dtos.auth.requests.UpdateProfileRequestDTO;
import org.example.prm392_groupprojectbe.entities.Account;
import org.example.prm392_groupprojectbe.enums.AccountRoleEnum;
import org.example.prm392_groupprojectbe.enums.AccountStatusEnum;
import org.example.prm392_groupprojectbe.exceptions.AppException;
import org.example.prm392_groupprojectbe.exceptions.ErrorCode;
import org.example.prm392_groupprojectbe.mappers.AccountMapper;
import org.example.prm392_groupprojectbe.repositories.AccountRepository;
import org.example.prm392_groupprojectbe.services.AccountService;
import org.example.prm392_groupprojectbe.utils.AccountUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<AccountResponseDTO> getUsers(int page, int size, String search, AccountRoleEnum role, AccountStatusEnum status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Account> accounts = accountRepository.findByFilters(search, role, status, pageable);
        return accounts.map(accountMapper::toDTO);
    }

    @Override
    public List<AccountResponseDTO> getAllUsers() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(accountMapper::toDTO).toList();
    }

    @Override
    public AccountResponseDTO getUserById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return accountMapper.toDTO(account);
    }

    @Override
    public void banUser(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        account.setBanned(true);
        accountRepository.save(account);
    }

    @Override
    public void unbanUser(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        account.setBanned(false);
        accountRepository.save(account);
    }

    @Override
    public void changePassword(ChangePasswordRequestDTO request) {
        Account account = AccountUtils.getCurrentAccount();
        if (account == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(request.getOldPassword(), account.getPassword())) {
            throw new AppException(ErrorCode.OLD_PASSWORD_INCORRECT);
        }

        // Cập nhật mật khẩu mới
        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);
    }

    @Override
    public AccountResponseDTO updateUser(Long accountId, UpdateProfileRequestDTO requestDTO) {
        Account currentAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (requestDTO.getName() != null) {
            currentAccount.setName(requestDTO.getName());
        }
        if (requestDTO.getPhoneNumber() != null) {
            currentAccount.setPhoneNumber(requestDTO.getPhoneNumber());
        }
        if (requestDTO.getGender() != null) {
            currentAccount.setGender(requestDTO.getGender());
        }
        if (requestDTO.getAvatar() != null) {
            currentAccount.setAvatar(requestDTO.getAvatar());
        }

        Account result = accountRepository.save(currentAccount);
        return accountMapper.toDTO(result);
    }

    @Override
    public Account findById(Long userId) {
        return accountRepository.findById(userId).orElse(null);
    }
}
