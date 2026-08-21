package com.banking.banking_management_system.service.impl;

import com.banking.banking_management_system.dto.response.audit.AuditLogResponse;
import com.banking.banking_management_system.mapper.AuditLogMapper;
import com.banking.banking_management_system.repository.AuditLogRepository;
import com.banking.banking_management_system.service.AdminAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminAuditServiceImpl implements AdminAuditService {
    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getAuditLogs(Pageable pageable) {
        return auditLogRepository
                .findAllByOrderByCreatedAtDesc(pageable)
                .map(auditLogMapper::toResponse);
    }
}
