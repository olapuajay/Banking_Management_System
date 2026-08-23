package com.banking.banking_management_system.service.impl;

import com.banking.banking_management_system.dto.response.customer.CustomerResponse;
import com.banking.banking_management_system.entity.Customer;
import com.banking.banking_management_system.exception.ResourceNotFoundException;
import com.banking.banking_management_system.mapper.CustomerMapper;
import com.banking.banking_management_system.repository.CustomerRepository;
import com.banking.banking_management_system.service.AdminCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminCustomerServiceImpl implements AdminCustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getAllCustomers(Pageable pageable) {
        return customerRepository
                .findAll(pageable)
                .map(customerMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return customerMapper.toResponse(customer);
    }
}
