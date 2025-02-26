package org.example.prm392_groupprojectbe.dtos.auth.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthResponseDTO {
    private String token;
}
