package com.banking.banking_management_system.controller;

import com.banking.banking_management_system.dto.request.beneficiary.AddBeneficiaryRequest;
import com.banking.banking_management_system.dto.response.beneficiary.BeneficiaryResponse;
import com.banking.banking_management_system.service.BeneficiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {
    private final BeneficiaryService beneficiaryService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BeneficiaryResponse> addBeneficiary(@Valid @RequestBody AddBeneficiaryRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(beneficiaryService.addBeneficiary(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<BeneficiaryResponse>> getMyBeneficiaries() {
        return ResponseEntity.ok(beneficiaryService.getMyBeneficiaries());
    }

    @GetMapping("/{beneficiaryId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BeneficiaryResponse> getBeneficiary(@PathVariable Long beneficiaryId) {
        return ResponseEntity.ok(beneficiaryService.getBeneficiary(beneficiaryId));
    }

    @PutMapping("/{beneficiaryId}/activate")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BeneficiaryResponse> activateBeneficiary(@PathVariable Long beneficiaryId) {
        return ResponseEntity.ok(beneficiaryService.activateBeneficiary(beneficiaryId));
    }

    @DeleteMapping("/{beneficiaryId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> removeBeneficiary(@PathVariable Long beneficiaryId) {
        beneficiaryService.removeBeneficiary(beneficiaryId);

        return ResponseEntity.noContent().build();
    }
}
