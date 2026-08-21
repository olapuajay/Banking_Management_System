package com.banking.banking_management_system.repository;

import com.banking.banking_management_system.entity.AuditLog;
import com.banking.banking_management_system.enums.AuditAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Page<AuditLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<AuditLog> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<AuditLog> findByActionOrderByCreatedAtDesc(AuditAction action, Pageable pageable);
}
