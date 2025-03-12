package org.example.prm392_groupprojectbe.mappers;

import org.example.prm392_groupprojectbe.dtos.accounts.AccountResponseDTO;
import org.example.prm392_groupprojectbe.entities.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountResponseDTO toDTO(Account account) {
        return AccountResponseDTO.builder()
                .id(account.getId())
                .name(account.getName())
                .email(account.getEmail())
                .phoneNumber(account.getPhoneNumber())
                .avatar(account.getAvatar())
                .role(account.getRole())
                .status(account.getStatus())
                .gender(account.getGender())
                .isBanned(account.isBanned())
                .build();
    }
}
