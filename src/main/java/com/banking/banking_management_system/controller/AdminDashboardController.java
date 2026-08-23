package com.banking.banking_management_system.controller;

import com.banking.banking_management_system.dto.response.account.AccountResponse;
import com.banking.banking_management_system.dto.response.admin.AdminDashboardResponse;
import com.banking.banking_management_system.dto.response.customer.CustomerResponse;
import com.banking.banking_management_system.dto.response.transaction.TransactionResponse;
import com.banking.banking_management_system.repository.AccountRepository;
import com.banking.banking_management_system.service.AdminAccountService;
import com.banking.banking_management_system.service.AdminCustomerService;
import com.banking.banking_management_system.service.AdminDashboardService;
import com.banking.banking_management_system.service.AdminTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {
    private final AdminDashboardService adminDashboardService;

    private final AdminCustomerService adminCustomerService;

    private final AdminAccountService adminAccountService;

    private final AdminTransactionService adminTransactionService;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> getDashboard() {
        return ResponseEntity.ok(
                adminDashboardService.getDashboard()
        );
    }

//    Customer management
    @GetMapping("/customers")
    public ResponseEntity<Page<CustomerResponse>> getAllCustomer(Pageable pageable) {
        return ResponseEntity.ok(
                adminCustomerService.getAllCustomers(pageable)
        );
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(
                adminCustomerService.getCustomer(customerId)
        );
    }

//    Account Management
    @GetMapping("/accounts")
    public ResponseEntity<Page<AccountResponse>> getAllAccounts(Pageable pageable) {
        return ResponseEntity.ok(adminAccountService.getAllAccounts(pageable));
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(
                adminAccountService.getAccount(accountId)
        );
    }

    @PutMapping("/accounts/{accountId}/freeze")
    public ResponseEntity<AccountResponse> freezeAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(
                adminAccountService.freezeAccount(accountId)
        );
    }

    @PutMapping("/accounts/{accountId}/unfreeze")
    public ResponseEntity<AccountResponse> unfreezeAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(
                adminAccountService.unfreezeAccount(accountId)
        );
    }

    @PutMapping("/accounts/{accountId}/close")
    public ResponseEntity<AccountResponse> closeAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(
                adminAccountService.closeAccount(accountId)
        );
    }

//    Transaction Management
    @GetMapping("/transactions")
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(Pageable pageable) {
        return ResponseEntity.ok(
                adminTransactionService.getAllTransactions(pageable)
        );
    }

    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable Long transactionId) {
        return ResponseEntity.ok(
                adminTransactionService.getTransaction(transactionId)
        );
    }
}
