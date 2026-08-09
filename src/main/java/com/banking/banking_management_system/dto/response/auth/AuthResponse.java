package com.banking.banking_management_system.dto.response.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private String accessToken;

    private String tokenType;

    private Long expiresIn;
}
