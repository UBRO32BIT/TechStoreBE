package org.example.prm392_groupprojectbe.dtos.auth.requests;

import lombok.Builder;
import lombok.Data;
import org.example.prm392_groupprojectbe.enums.AccountGenderEnum;

@Data
@Builder
public class RegisterRequestDTO {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private AccountGenderEnum gender;
}
