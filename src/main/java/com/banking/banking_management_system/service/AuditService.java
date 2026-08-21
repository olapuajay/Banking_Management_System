package com.banking.banking_management_system.service;

import com.banking.banking_management_system.entity.User;
import com.banking.banking_management_system.enums.AuditAction;
import com.banking.banking_management_system.enums.AuditResult;

public interface AuditService {
    void log(
            User user,
            AuditAction action,
            AuditResult result,
            String resourceType,
            Long resourceId,
            String description,
            String ipAddress
    );
}
