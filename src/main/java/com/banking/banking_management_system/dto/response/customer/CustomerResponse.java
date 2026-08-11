package com.banking.banking_management_system.dto.response.customer;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CustomerResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String address;
    private String maskedAadhaarNumber;
    private String maskedPanNumber;
    private boolean kycVerified;
}
