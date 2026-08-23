package com.banking.banking_management_system.service;

import com.banking.banking_management_system.dto.response.customer.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminCustomerService {
    Page<CustomerResponse> getAllCustomers(Pageable pageable);

    CustomerResponse getCustomer(Long customerId);
}
