package com.banking.banking_management_system.mapper;

import com.banking.banking_management_system.dto.response.beneficiary.BeneficiaryResponse;
import com.banking.banking_management_system.entity.Beneficiary;
import org.springframework.stereotype.Component;

@Component
public class BeneficiaryMapper {
    public BeneficiaryResponse toResponse(Beneficiary beneficiary) {
        return BeneficiaryResponse.builder()
                .id(beneficiary.getId())
                .name(beneficiary.getName())
                .accountNumber(beneficiary.getAccountNumber())
                .ifscCode(beneficiary.getIfscCode())
                .status(beneficiary.getStatus())
                .createdAt(beneficiary.getCreatedAt())
                .build();
    }
}
