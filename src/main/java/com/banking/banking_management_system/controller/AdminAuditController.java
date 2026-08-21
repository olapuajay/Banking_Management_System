package com.banking.banking_management_system.controller;

import com.banking.banking_management_system.dto.response.audit.AuditLogResponse;
import com.banking.banking_management_system.service.AdminAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/audit-logs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAuditController {
    private final AdminAuditService adminAuditService;

    @GetMapping
    public ResponseEntity<Page<AuditLogResponse>> getAuditLogs(Pageable pageable) {
        return ResponseEntity.ok(
                adminAuditService.getAuditLogs(pageable)
        );
    }
}
