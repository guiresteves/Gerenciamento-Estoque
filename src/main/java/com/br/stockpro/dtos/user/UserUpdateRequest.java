package com.br.stockpro.dtos.user;

import com.br.stockpro.enums.Role;
import com.br.stockpro.validation.annotation.ValidCPF;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(

        @Size(min = 3, max = 150)
        String name,

        @ValidCPF
        String cpf,

        Role role
) {
}
