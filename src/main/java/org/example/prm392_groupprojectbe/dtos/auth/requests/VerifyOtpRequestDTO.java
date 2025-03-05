package org.example.prm392_groupprojectbe.dtos.auth.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpRequestDTO {
    private String email;
    private String otp;
}
