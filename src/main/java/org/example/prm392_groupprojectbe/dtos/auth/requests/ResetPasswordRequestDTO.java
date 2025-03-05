package org.example.prm392_groupprojectbe.dtos.auth.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequestDTO {
    private String newPassword;
    private String token;
}
