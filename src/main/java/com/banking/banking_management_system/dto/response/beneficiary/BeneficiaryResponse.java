package com.banking.banking_management_system.dto.response.beneficiary;

import com.banking.banking_management_system.enums.BeneficiaryStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BeneficiaryResponse {
    private Long id;
    private String name;
    private String accountNumber;
    private String ifscCode;
    private BeneficiaryStatus status;
    private LocalDateTime createdAt;
}
