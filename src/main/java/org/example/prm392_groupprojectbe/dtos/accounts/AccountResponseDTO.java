package org.example.prm392_groupprojectbe.dtos.accounts;

import lombok.*;
import org.example.prm392_groupprojectbe.enums.AccountGenderEnum;
import org.example.prm392_groupprojectbe.enums.AccountRoleEnum;
import org.example.prm392_groupprojectbe.enums.AccountStatusEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String avatar;
    private Boolean isBanned;
    private AccountRoleEnum role;
    private AccountStatusEnum status;
    private AccountGenderEnum gender;
}