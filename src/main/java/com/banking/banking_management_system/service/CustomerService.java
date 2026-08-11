package com.banking.banking_management_system.service;

import com.banking.banking_management_system.dto.request.customer.UpdateCustomerRequest;
import com.banking.banking_management_system.dto.response.customer.CustomerResponse;

public interface CustomerService {
    CustomerResponse getMyProfile();

    CustomerResponse updateMyProfile(UpdateCustomerRequest request);

    CustomerResponse getCustomerById(Long customerId);
}
