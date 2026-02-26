package com.br.stockpro.dtos.company;

import com.br.stockpro.enums.CompanyStatus;
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
        @Pattern(regexp = "//d{14}")
        String cnpj,

        @Size(min = 3, max = 50)
        String stateRegistration,

        @Size(min = 3, max = 50)
        String municipalRegistration,

        @NotNull
        CompanyType companyType,

        @NotBlank
        @Size(max = 20)
        String phone,

        @Email
        @NotBlank
        String email,

        @NotNull
        CompanyStatus companyStatus
) {
}
