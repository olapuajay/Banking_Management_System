package com.banking.banking_management_system.entity;

import com.banking.banking_management_system.enums.LoanStatus;
import com.banking.banking_management_system.enums.LoanType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(
        name = "loans",
        indexes = {
                @Index(name = "idx_loan_number", columnList = "loan_number"),
                @Index(name = "idx_loan_customer", columnList = "customer_id")
        }
)
public class Loan extends BaseEntity {
    @Column(name = "loan_number", nullable = false, unique = true, length = 20)
    private String loanNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanType loanType;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal principalAmount;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private Integer tenureMonths;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal emiAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal outstandingAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;

    private LocalDate applicationDate;

    private LocalDate approvalDate;

    private LocalDate maturityDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}
