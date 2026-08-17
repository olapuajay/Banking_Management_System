package com.banking.banking_management_system.entity;

import com.banking.banking_management_system.enums.BeneficiaryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "beneficiaries",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_customer_beneficiary_account",
                        columnNames = {"customer_id", "account_number"}
                )
        },
        indexes = {
                @Index(name = "idx_beneficiary_customer", columnList = "customer_id")
        }
)
public class Beneficiary extends BaseEntity {
    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "account_number", nullable = false, length = 16)
    private String accountNumber;

    @Column(name = "ifsc_code", nullable = false, length = 11)
    private String ifscCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BeneficiaryStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}
