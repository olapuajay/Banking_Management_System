package com.banking.banking_management_system.controller;

import com.banking.banking_management_system.dto.request.customer.UpdateCustomerRequest;
import com.banking.banking_management_system.dto.response.customer.CustomerResponse;
import com.banking.banking_management_system.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerResponse> getMyProfile() {
        return ResponseEntity.ok(customerService.getMyProfile());
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerResponse> updateMyProfile(@Valid @RequestBody UpdateCustomerRequest request) {
        return ResponseEntity.ok(customerService.updateMyProfile(request));
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }
}
