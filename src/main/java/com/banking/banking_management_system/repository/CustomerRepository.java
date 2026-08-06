package com.banking.banking_management_system.repository;

import com.banking.banking_management_system.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUserId(Long userId);
    Optional<Customer> findByPhone(String phone);
    boolean existsByPhone(String phone);
    boolean existsByAadhaarNumber(String aadhaarNumber);
    boolean existsByPanNumber(String panNumber);
}
