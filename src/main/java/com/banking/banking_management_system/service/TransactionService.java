package com.banking.banking_management_system.service;

import com.banking.banking_management_system.dto.request.transaction.DepositRequest;
import com.banking.banking_management_system.dto.request.transaction.TransferRequest;
import com.banking.banking_management_system.dto.request.transaction.WithdrawRequest;
import com.banking.banking_management_system.dto.response.transaction.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    TransactionResponse deposit(DepositRequest request);
    TransactionResponse withdraw(WithdrawRequest request);
    TransactionResponse transfer(TransferRequest request);
    Page<TransactionResponse> getMyTransactionHistory(Pageable pageable);
}
