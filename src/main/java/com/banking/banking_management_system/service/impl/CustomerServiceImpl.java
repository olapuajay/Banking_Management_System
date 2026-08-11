package com.banking.banking_management_system.service.impl;

import com.banking.banking_management_system.dto.request.customer.UpdateCustomerRequest;
import com.banking.banking_management_system.dto.response.customer.CustomerResponse;
import com.banking.banking_management_system.entity.Customer;
import com.banking.banking_management_system.entity.User;
import com.banking.banking_management_system.exception.DuplicateResourceException;
import com.banking.banking_management_system.exception.ResourceNotFoundException;
import com.banking.banking_management_system.mapper.CustomerMapper;
import com.banking.banking_management_system.repository.CustomerRepository;
import com.banking.banking_management_system.repository.UserRepository;
import com.banking.banking_management_system.service.CustomerService;
import com.banking.banking_management_system.util.SecurityUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getMyProfile() {
        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Customer customer = customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));

        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse updateMyProfile(UpdateCustomerRequest request) {
        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Customer customer = customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));

        if(request.getPhone() != null && !request.getPhone().equals(customer.getPhone()) && customerRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Phone number is already registered");
        }

        if(request.getFirstName() != null) {
            customer.setFirstName(request.getFirstName());
        }

        if(request.getLastName() != null) {
            customer.setLastName(request.getLastName());
        }

        if(request.getPhone() != null) {
            customer.setPhone(request.getPhone());
        }

        if(request.getDateOfBirth() != null) {
            customer.setDateOfBirth(request.getDateOfBirth());
        }

        if(request.getAddress() != null) {
            customer.setAddress(request.getAddress());
        }

        Customer updatedCustomer = customerRepository.save(customer);

        return customerMapper.toResponse(updatedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        return customerMapper.toResponse(customer);
    }
}
