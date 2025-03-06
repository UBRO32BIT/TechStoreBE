package org.example.prm392_groupprojectbe.dtos.auth.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDTO {
    private String oldPassword;
    private String newPassword;
} 