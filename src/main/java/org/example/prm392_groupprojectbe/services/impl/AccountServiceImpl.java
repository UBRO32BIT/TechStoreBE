package org.example.prm392_groupprojectbe.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.dtos.accounts.AccountResponseDTO;
import org.example.prm392_groupprojectbe.entities.Account;
import org.example.prm392_groupprojectbe.enums.AccountRoleEnum;
import org.example.prm392_groupprojectbe.enums.AccountStatusEnum;
import org.example.prm392_groupprojectbe.mappers.AccountMapper;
import org.example.prm392_groupprojectbe.repositories.AccountRepository;
import org.example.prm392_groupprojectbe.services.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public Page<AccountResponseDTO> getUsers(int page, int size, String search, AccountRoleEnum role, AccountStatusEnum status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Account> accounts = accountRepository.findByFilters(search, role, status, pageable);
        return accounts.map(accountMapper::toDTO);
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
}
