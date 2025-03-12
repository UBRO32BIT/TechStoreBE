package org.example.prm392_groupprojectbe.services;

import org.example.prm392_groupprojectbe.dtos.accounts.AccountResponseDTO;
import org.example.prm392_groupprojectbe.dtos.auth.requests.ChangePasswordRequestDTO;
import org.example.prm392_groupprojectbe.dtos.auth.requests.UpdateProfileRequestDTO;
import org.example.prm392_groupprojectbe.entities.Account;
import org.example.prm392_groupprojectbe.enums.AccountRoleEnum;
import org.example.prm392_groupprojectbe.enums.AccountStatusEnum;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AccountService {
    Page<AccountResponseDTO> getUsers(int page, int size, String search, AccountRoleEnum role, AccountStatusEnum status);

    List<AccountResponseDTO> getAllUsers();

    AccountResponseDTO getUserById(Long id);

    void banUser(Long id);

    void unbanUser(Long id);

    void changePassword(Long userId, ChangePasswordRequestDTO request);

    AccountResponseDTO updateUser(Long accountId, UpdateProfileRequestDTO requestDTO);

    Account findById(Long userId);
}
