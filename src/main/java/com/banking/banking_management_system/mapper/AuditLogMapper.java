package com.banking.banking_management_system.mapper;

import com.banking.banking_management_system.dto.response.audit.AuditLogResponse;
import com.banking.banking_management_system.entity.AuditLog;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {
    public AuditLogResponse toResponse(AuditLog auditLog) {
        return AuditLogResponse.builder()
                .id(auditLog.getId())
                .username(auditLog.getUser() != null ? auditLog.getUser().getEmail() : "SYSTEM")
                .action(auditLog.getAction())
                .result(auditLog.getResult())
                .resourceType(auditLog.getResourceType())
                .resourceId(auditLog.getResourceId())
                .ipAddress(auditLog.getIpAddress())
                .description(auditLog.getDescription())
                .errorMessage(auditLog.getErrorMessage())
                .createdAt(auditLog.getCreatedAt())
                .build();
    }
}
