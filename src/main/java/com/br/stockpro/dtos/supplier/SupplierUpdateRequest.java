package com.br.stockpro.dtos.supplier;

import jakarta.validation.constraints.*;

public record SupplierUpdateRequest(

        @Size(min = 3, max = 250, message = "Razão social deve ter entre 3 e 250 caracteres")
        String legalName,

        @Size(min = 3, max = 250, message = "Nome fantasia deve ter entre 3 e 250 caracteres")
        String tradeName,

        @Size(max = 50, message = "Inscrição estadual deve ter no máximo 50 caracteres")
        String stateRegistration,

        @Size(max = 50, message = "Inscrição municipal deve ter no máximo 50 caracteres")
        String municipalRegistration,

        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        @Pattern(regexp = "[0-9()\\-\\s+]+", message = "Telefone inválido")
        String phone,

        @Email(message = "Email inválido")
        @Size(max = 150, message = "Email deve ter no máximo 150 caracteres")
        String email
) {
}
