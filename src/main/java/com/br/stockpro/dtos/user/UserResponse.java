package com.br.stockpro.dtos.user;

import com.br.stockpro.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "Dados do usuário retornados pela API")
public record UserResponse(

        @Schema(description = "ID do usuário", example = "1")
        Long id,

        @Schema(description = "Nome completo do usuário", example = "João Silva")
        String name,

        @Schema(description = "Email do usuário", example = "joao@mercado.com")
        String email,

        @Schema(description = "CPF do usuário", example = "12345678909", nullable = true)
        String cpf,

        @Schema(description = "Role do usuário no sistema",
                allowableValues = {"ADMIN", "MANAGER", "STOCKER", "CASHIER", "FINANCIAL"},
                example = "STOCKER")
        Role role,

        @Schema(description = "Status do usuário", example = "true")
        Boolean active,

        @Schema(description = "ID da empresa vinculada ao usuário", example = "1")
        Long companyId,

        @Schema(description = "Data de criação no formato ISO 8601", example = "2024-01-15T10:30:00Z")
        Instant createdAt,

        @Schema(description = "Data da última atualização no formato ISO 8601", example = "2024-01-15T10:30:00Z")
        Instant updatedAt
) {}