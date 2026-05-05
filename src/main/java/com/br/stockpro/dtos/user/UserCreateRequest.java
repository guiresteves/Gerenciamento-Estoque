package com.br.stockpro.dtos.user;

import com.br.stockpro.enums.Role;
import com.br.stockpro.validation.annotation.ValidCPF;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados para criação de um novo usuário vinculado à empresa")
public record UserCreateRequest(

        @Schema(description = "Nome completo do usuário", example = "João Silva")
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 50)
        String name,

        @Schema(description = "Email do usuário — deve ser único no sistema", example = "joao@mercado.com")
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @Schema(description = "Senha de acesso — mínimo 8 caracteres com letras maiúsculas, minúsculas e números",
         example = "Joao1234")
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        @Pattern(
         regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
         message = "Senha deve conter letras maiúsculas, minúsculas e números"
        )
        String password,

        @Schema(description = "CPF do usuário — validado matematicamente", example = "12345678909", nullable = true)
        @ValidCPF
        String cpf,

        @Schema(description = """
                Role do usuário — define as permissões no sistema:
                - `ADMIN` → acesso total
                - `MANAGER` → gerencia produtos, categorias, fornecedores e estoque
                - `STOCKER` → gerencia estoque e lotes
                - `CASHIER` → consultas e saídas no caixa
                - `FINANCIAL` → apenas consultas e relatórios
                """,
                 allowableValues = {"ADMIN", "MANAGER", "STOCKER", "CASHIER", "FINANCIAL"},
                 example = "STOCKER")

        @NotNull(message = "Role é obrigatória")
        Role role
) {}