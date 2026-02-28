package com.br.stockpro.dtos.company;

import com.br.stockpro.enums.CompanyType;
import jakarta.validation.constraints.*;

public record CompanyCreateRequest(

        @Size(min = 3, max = 250)
        @NotBlank
        String legalName,

        @Size(min = 3, max = 250)
        @NotBlank
        String tradeName,

        @NotBlank
        @Pattern(regexp = "\\d{14}", message = "CNPJ deve conter 14 dígitos numéricos")
        String cnpj,

        @Size(max = 50)
        String stateRegistration,

        @Size(max = 50)
        String municipalRegistration,

        @NotNull
        CompanyType companyType,

        @NotBlank
        @Size(max = 20)
        String phone,

        @Email
        @NotBlank
        String email
) {
}
