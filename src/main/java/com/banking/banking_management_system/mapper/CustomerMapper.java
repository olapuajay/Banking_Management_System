package com.banking.banking_management_system.mapper;

import com.banking.banking_management_system.dto.response.customer.CustomerResponse;
import com.banking.banking_management_system.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .userId(customer.getUser().getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getUser().getEmail())
                .phone(customer.getPhone())
                .dateOfBirth(customer.getDateOfBirth())
                .address(customer.getAddress())
                .maskedAadhaarNumber(maskAadhaar(customer.getAadhaarNumber()))
                .maskedPanNumber(maskPan(customer.getPanNumber()))
                .kycVerified(customer.isKycVerified())
                .build();
    }

    private String maskAadhaar(String aadhaar) {
        if(aadhaar == null || aadhaar.length() != 12) {
            return null;
        }

        return "XXXX-XXXX-" + aadhaar.substring(8);
    }

    private String maskPan(String pan) {
        if(pan == null || pan.length() != 10) {
            return null;
        }

        return pan.substring(0, 2) + "XXXXXX" + pan.substring(8);
    }
}
