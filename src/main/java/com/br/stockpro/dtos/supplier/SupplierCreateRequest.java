package com.br.stockpro.dtos.supplier;

import jakarta.validation.constraints.*;

public record SupplierCreateRequest(

        @Size(min = 3, max = 250, message = "Razão Social deve ter entre 3 a 250 caracteres")
        @NotBlank(message = "Razão social é obrigatório")
        String legalName,

        @Size(min = 3, max = 250, message = "Nome fantasia deve ter entre 3 a 250 caracteres")
        @NotBlank(message = "Nome Fantasia é obrigatório")
        String tradeName,

        @NotBlank(message = "CNPJ é obrigatório")
        @Pattern(regexp = "\\d{14}", message = "CNPJ deve conter 14 dígitos numéricos")
        String cnpj,

        @Size(max = 50, message = "Inscrição estadual deve ter no máximo 50 caracteres")
        String stateRegistration,

        @Size(max = 50, message = "Inscrição estadual deve ter no máximo 50 caracteres")
        String municipalRegistration,

        @NotBlank(message = "Telefone é obrigatório")
        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        @Pattern(regexp = "[0-9()\\-\\s+]+", message = "Telefone Inválido")
        String phone,

        @Email(message = "Email Inválido")
        @NotBlank(message = "Email é obrigatório")
        @Size(max = 150, message = "O email deve ter no máximo 150 caracteres")
        String email
) {
}
