package com.banking.banking_management_system.entity;

import com.banking.banking_management_system.enums.TransactionStatus;
import com.banking.banking_management_system.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(
        name = "transactions",
        indexes = {
                @Index(name = "idx_transaction_reference", columnList = "reference_number"),
                @Index(name = "idx_transaction_source_account", columnList = "source_account_id"),
                @Index(name = "idx_transaction_destination_account", columnList = "destination_account_id")
        }
)
public class Transaction extends BaseEntity {
    @Column(
            name = "reference_number",
            nullable = false,
            unique = true,
            length = 30
    )
    private String referenceNumber;

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(length = 255)
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account_id")
    private Account sourceAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;
}
