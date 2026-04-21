package com.br.stockpro.dtos.user;

import jakarta.validation.constraints.*;

public record ChangePasswordRequest(

        @NotBlank(message = "Senha atual é obrigatória")
        String currentPassword,

        @NotBlank(message = "Nova senha é obrigatória")
        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
                message = "Senha deve conter letras maiúsculas, minúsculas e números"
        )
        String newPassword
) {
}
