package com.banking.banking_management_system.service.impl;

import com.banking.banking_management_system.entity.AuditLog;
import com.banking.banking_management_system.entity.User;
import com.banking.banking_management_system.enums.AuditAction;
import com.banking.banking_management_system.enums.AuditResult;
import com.banking.banking_management_system.repository.AuditLogRepository;
import com.banking.banking_management_system.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(
            User user,
            AuditAction action,
            AuditResult result,
            String resourceType,
            Long resourceId,
            String description,
            String ipAddress
    ) {
        AuditLog auditLog = new AuditLog();

        auditLog.setUser(user);
        auditLog.setAction(action);
        auditLog.setResult(result);
        auditLog.setResourceType(resourceType);
        auditLog.setResourceId(resourceId);
        auditLog.setDescription(description);
        auditLog.setIpAddress(ipAddress);

        auditLogRepository.save(auditLog);
    }
}
