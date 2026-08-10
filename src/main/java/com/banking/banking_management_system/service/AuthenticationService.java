package com.banking.banking_management_system.service;

import com.banking.banking_management_system.dto.request.auth.RegisterRequest;
import com.banking.banking_management_system.dto.response.auth.RegisterResponse;

public interface AuthenticationService {
    RegisterResponse register(RegisterRequest request);
}
