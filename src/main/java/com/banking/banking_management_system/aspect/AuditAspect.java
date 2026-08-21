package com.banking.banking_management_system.aspect;

import com.banking.banking_management_system.annotation.Auditable;
import com.banking.banking_management_system.entity.User;
import com.banking.banking_management_system.enums.AuditResult;
import com.banking.banking_management_system.repository.UserRepository;
import com.banking.banking_management_system.service.AuditService;
import com.banking.banking_management_system.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditService auditService;
    private final UserRepository userRepository;
    private final HttpServletRequest request;

    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        try {
            Object result = joinPoint.proceed();

            createAuditLog(auditable, AuditResult.SUCCESS, null);

            return result;
        } catch (Exception exception) {
            createAuditLog(auditable, AuditResult.FAILURE, exception.getMessage());

            throw exception;
        }
    }

    private void createAuditLog(Auditable auditable, AuditResult result, String errorMessage) {
        String email = SecurityUtils.getCurrentUserEmail();

        if(email == null) {
            return;
        }

        User user = userRepository.findByEmail(email)
                .orElse(null);

        String ipAddress = request.getRemoteAddr();

        auditService.log(
                user,
                auditable.action(),
                result,
                auditable.resourceType(),
                null,
                errorMessage,
                ipAddress
        );
    }
}
