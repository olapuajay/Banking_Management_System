package com.banking.banking_management_system.annotation;

import com.banking.banking_management_system.enums.AuditAction;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {
    AuditAction action();
    String resourceType();
}
