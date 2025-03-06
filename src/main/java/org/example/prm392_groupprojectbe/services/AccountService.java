package org.example.prm392_groupprojectbe.services;

import org.example.prm392_groupprojectbe.dtos.accounts.AccountResponseDTO;
import org.example.prm392_groupprojectbe.enums.AccountRoleEnum;
import org.example.prm392_groupprojectbe.enums.AccountStatusEnum;
import org.springframework.data.domain.Page;

public interface AccountService {
    Page<AccountResponseDTO> getUsers(int page, int size, String search, AccountRoleEnum role, AccountStatusEnum status);

    AccountResponseDTO getUserById(Long id);

    void banUser(Long id);

    void unbanUser(Long id);

    void changePassword(Long userId, String oldPassword, String newPassword);
    
    Account findById(Long userId);
}
