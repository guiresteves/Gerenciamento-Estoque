package com.br.stockpro.security.anotations;

import org.springframework.security.access.prepost.PreAuthorize;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STOCKER', 'CASHIER', 'FINANCIAL')")
public @interface CanViewStock {
}
