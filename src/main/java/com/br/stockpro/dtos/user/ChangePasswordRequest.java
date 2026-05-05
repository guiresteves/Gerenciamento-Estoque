package com.br.stockpro.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados para alteração de senha do usuário autenticado")
public record ChangePasswordRequest(

        @Schema(description = "Senha atual do usuário — necessária para confirmar a operação",
                example = "Admin123")
        @NotBlank(message = "Senha atual é obrigatória")
        String currentPassword,

        @Schema(description = "Nova senha — mínimo 8 caracteres com letras maiúsculas, minúsculas e números",
                example = "NovaAdmin123")
        @NotBlank(message = "Nova senha é obrigatória")
        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
                message = "Senha deve conter letras maiúsculas, minúsculas e números"
        )
        String newPassword
) {}