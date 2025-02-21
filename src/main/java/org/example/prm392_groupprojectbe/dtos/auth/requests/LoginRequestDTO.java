package org.example.prm392_groupprojectbe.dtos.auth.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDTO {
    private String email;
    private String password;
}
