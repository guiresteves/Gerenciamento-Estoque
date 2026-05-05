package com.br.stockpro.dtos.user;

import com.br.stockpro.enums.Role;
import com.br.stockpro.validation.annotation.ValidCPF;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualização de um usuário — todos os campos são opcionais")
public record UserUpdateRequest(

        @Schema(description = "Novo nome do usuário", example = "João Silva Santos")
        @Size(min = 3, max = 150)
        String name,

        @Schema(description = "Novo CPF do usuário — validado matematicamente",
                example = "12345678909", nullable = true)
        @ValidCPF
        String cpf,

        @Schema(description = "Nova role do usuário",
                allowableValues = {"ADMIN", "MANAGER", "STOCKER", "CASHIER", "FINANCIAL"},
                example = "MANAGER")
        Role role
) {}