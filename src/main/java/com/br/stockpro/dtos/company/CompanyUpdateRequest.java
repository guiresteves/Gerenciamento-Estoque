package com.br.stockpro.dtos.company;

import com.br.stockpro.enums.CompanyType;
import jakarta.validation.constraints.*;

public record CompanyUpdateRequest(

        @NotBlank(message = "Razão social é obrigatória")
        @Size(min = 3, max = 250, message = "Razão social deve ter entre 3 e 250 caracteres")
        String legalName,

        @NotBlank(message = "Nome fantasia é obrigatório")
        @Size(min = 3, max = 250, message = "Nome fantasia deve ter entre 3 e 250 caracteres")
        String tradeName,

        @Size(max = 50, message = "Inscrição estadual deve ter no máximo 50 caracteres")
        String stateRegistration,

        @Size(max = 50, message = "Inscrição municipal deve ter no máximo 50 caracteres")
        String municipalRegistration,

        @NotNull(message = "Tipo da empresa é obrigatório")
        CompanyType companyType,

        @NotBlank(message = "Telefone é obrigatório")
        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        @Pattern(regexp = "[0-9()\\-\\s+]+", message = "Telefone inválido")
        String phone,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Size(max = 150, message = "Email deve ter no máximo 150 caracteres")
        String email

) {
}
