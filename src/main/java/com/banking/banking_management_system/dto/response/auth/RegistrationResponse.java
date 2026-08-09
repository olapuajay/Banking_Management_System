package com.banking.banking_management_system.dto.response.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegistrationResponse {
    private Long userId;

    private String email;

    private String message;
}
