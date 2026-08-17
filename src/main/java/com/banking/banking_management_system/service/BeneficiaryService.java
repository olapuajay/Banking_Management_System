package com.banking.banking_management_system.service;

import com.banking.banking_management_system.dto.request.beneficiary.AddBeneficiaryRequest;
import com.banking.banking_management_system.dto.response.beneficiary.BeneficiaryResponse;

import java.util.List;

public interface BeneficiaryService {
    BeneficiaryResponse addBeneficiary(AddBeneficiaryRequest request);

    List<BeneficiaryResponse> getMyBeneficiaries();

    BeneficiaryResponse getBeneficiary(Long beneficiaryId);

    BeneficiaryResponse activateBeneficiary(Long beneficiaryId);

    void removeBeneficiary(Long beneficiaryId);
}
