package com.banking.banking_management_system.service.impl;

import com.banking.banking_management_system.annotation.Auditable;
import com.banking.banking_management_system.dto.request.beneficiary.AddBeneficiaryRequest;
import com.banking.banking_management_system.dto.response.beneficiary.BeneficiaryResponse;
import com.banking.banking_management_system.entity.Beneficiary;
import com.banking.banking_management_system.entity.Customer;
import com.banking.banking_management_system.entity.User;
import com.banking.banking_management_system.enums.AuditAction;
import com.banking.banking_management_system.enums.BeneficiaryStatus;
import com.banking.banking_management_system.exception.DuplicateResourceException;
import com.banking.banking_management_system.exception.InvalidTransactionException;
import com.banking.banking_management_system.exception.ResourceNotFoundException;
import com.banking.banking_management_system.mapper.BeneficiaryMapper;
import com.banking.banking_management_system.repository.BeneficiaryRepository;
import com.banking.banking_management_system.repository.CustomerRepository;
import com.banking.banking_management_system.repository.UserRepository;
import com.banking.banking_management_system.service.BeneficiaryService;
import com.banking.banking_management_system.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class BeneficiaryServiceImpl implements BeneficiaryService {
    private final BeneficiaryRepository beneficiaryRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final BeneficiaryMapper beneficiaryMapper;

    @Auditable(action = AuditAction.ADD_BENEFICIARY, resourceType = "BENEFICIARY")
    @Override
    @Transactional
    public BeneficiaryResponse addBeneficiary(AddBeneficiaryRequest request) {
        Customer customer = getCurrentCustomer();

        if(beneficiaryRepository.existsByCustomerIdAndAccountNumber(customer.getId(), request.getAccountNumber())) {
            throw new DuplicateResourceException("Beneficiary already exists");
        }

        Beneficiary beneficiary = new Beneficiary();

        beneficiary.setName(request.getName());
        beneficiary.setAccountNumber(request.getAccountNumber());
        beneficiary.setIfscCode(request.getIfscCode());
        beneficiary.setStatus(BeneficiaryStatus.PENDING);
        beneficiary.setCustomer(customer);

        Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);

        return beneficiaryMapper.toResponse(savedBeneficiary);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeneficiaryResponse> getMyBeneficiaries() {
        Customer customer = getCurrentCustomer();

        return beneficiaryRepository
                .findByCustomerId(customer.getId())
                .stream()
                .map(beneficiaryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BeneficiaryResponse getBeneficiary(Long beneficiaryId) {
        Customer customer = getCurrentCustomer();

        Beneficiary beneficiary = beneficiaryRepository
                .findByIdAndCustomerId(beneficiaryId, customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found"));

        return beneficiaryMapper.toResponse(beneficiary);
    }

    @Auditable(action = AuditAction.ACTIVATE_BENEFICIARY, resourceType = "BENEFICIARY")
    @Override
    @Transactional
    public BeneficiaryResponse activateBeneficiary(Long beneficiaryId) {
        Customer customer = getCurrentCustomer();

        Beneficiary beneficiary = beneficiaryRepository
                .findByIdAndCustomerId(beneficiaryId, customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found"));

        if(beneficiary.getStatus() == BeneficiaryStatus.REMOVED) {
            throw new InvalidTransactionException("Removed beneficiary cannot be activated");
        }

        beneficiary.setStatus(BeneficiaryStatus.ACTIVE);

        return beneficiaryMapper.toResponse(beneficiary);
    }

    @Auditable(action = AuditAction.REMOVE_BENEFICIARY, resourceType = "BENEFICIARY")
    @Override
    @Transactional
    public void removeBeneficiary(Long beneficiaryId) {
        Customer customer = getCurrentCustomer();

        Beneficiary beneficiary = beneficiaryRepository
                .findByIdAndCustomerId(beneficiaryId, customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found"));

        beneficiary.setStatus(BeneficiaryStatus.REMOVED);
    }

    private Customer getCurrentCustomer() {
        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return customerRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));
    }
}
