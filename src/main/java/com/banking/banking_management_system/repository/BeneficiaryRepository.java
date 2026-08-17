package com.banking.banking_management_system.repository;

import com.banking.banking_management_system.entity.Beneficiary;
import com.banking.banking_management_system.enums.BeneficiaryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    List<Beneficiary> findByCustomerId(Long customerId);
    List<Beneficiary> findByCustomerIdAndStatus(Long customerId, BeneficiaryStatus status);
    Optional<Beneficiary> findByIdAndCustomerId(Long id, Long customerId);
    boolean existsByCustomerIdAndAccountNumber(Long customerId, String accountNumber);
    Optional<Beneficiary> findByCustomerIdAndAccountNumber(Long customerId, String accountNumber);
}
