package com.br.stockpro.dtos.user;

import com.br.stockpro.enums.Role;
import com.br.stockpro.validation.annotation.ValidCPF;
import jakarta.validation.constraints.*;

public record UserCrreateRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 50)
        String name,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
                message = "Senha deve conter letras maiúsculas, minúsculas e números"
        )
        String password,

        @ValidCPF
        String cpf,

        @NotNull(message = "Role é obrigatória")
        Role role
) {
}
