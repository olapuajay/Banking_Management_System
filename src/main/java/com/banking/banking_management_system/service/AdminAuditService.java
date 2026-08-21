package com.banking.banking_management_system.service;

import com.banking.banking_management_system.dto.response.audit.AuditLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminAuditService {
    Page<AuditLogResponse> getAuditLogs(Pageable pageable);
}
