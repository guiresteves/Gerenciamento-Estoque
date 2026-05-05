package com.br.stockpro.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para registro de novo usuário administrador")
public record RegisterRequest(

        @Schema(description = "Nome completo do usuário", example = "Admin StockPro")
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @Schema(description = "Email do usuário — deve ser único no sistema", example = "admin@stockpro.com")
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @Schema(description = "Senha de acesso — mínimo 8 caracteres com letras maiúsculas, minúsculas e números",
                example = "Admin123")
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
                message = "Senha deve conter letras maiúsculas, minúsculas e números"
        )
        String password
) {
}
