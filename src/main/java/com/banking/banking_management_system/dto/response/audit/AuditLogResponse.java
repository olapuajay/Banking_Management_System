package com.banking.banking_management_system.dto.response.audit;

import com.banking.banking_management_system.enums.AuditAction;
import com.banking.banking_management_system.enums.AuditResult;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AuditLogResponse {
    private Long id;
    private String username;
    private AuditAction action;
    private AuditResult result;
    private String resourceType;
    private Long resourceId;
    private String ipAddress;
    private String description;
    private String errorMessage;
    private LocalDateTime createdAt;
}
