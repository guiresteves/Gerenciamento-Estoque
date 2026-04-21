package com.br.stockpro.dtos.user;

import com.br.stockpro.enums.Role;

import java.time.Instant;

public record UserResponse(
        Long id,
        String name,
        String email,
        String cpf,
        Role role,
        Boolean active,
        Long companyId,
        Instant createdAt,
        Instant updatedAt
) {
}
