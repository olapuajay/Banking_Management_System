package com.banking.banking_management_system.service.impl;

import com.banking.banking_management_system.dto.request.auth.RegisterRequest;
import com.banking.banking_management_system.dto.response.auth.RegisterResponse;
import com.banking.banking_management_system.entity.Customer;
import com.banking.banking_management_system.entity.User;
import com.banking.banking_management_system.enums.Role;
import com.banking.banking_management_system.exception.DuplicateResourceException;
import com.banking.banking_management_system.repository.CustomerRepository;
import com.banking.banking_management_system.repository.UserRepository;
import com.banking.banking_management_system.service.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email is already registered");
        }

        if(customerRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Phone number is already registered");
        }

        User user = new User();

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        Customer customer = new Customer();

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setPhone(request.getPhone());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setAddress(request.getAddress());
        customer.setUser(savedUser);

        savedUser.setCustomer(customer);

        customerRepository.save(customer);

        return RegisterResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .message("Registration successful")
                .build();
    }
}
