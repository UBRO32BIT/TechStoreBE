package org.example.prm392_groupprojectbe.dtos.auth.requests;

import lombok.Getter;
import lombok.Setter;
import org.example.prm392_groupprojectbe.enums.AccountGenderEnum;

@Getter
@Setter
public class UpdateProfileRequestDTO {
    private String name;
    private String phoneNumber;
    private String avatar;
    private AccountGenderEnum gender;
}
