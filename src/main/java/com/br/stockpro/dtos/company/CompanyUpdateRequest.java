package com.br.stockpro.dtos.company;

import com.br.stockpro.enums.CompanyType;
import jakarta.validation.constraints.*;

public record CompanyUpdateRequest(

        @NotBlank
        @Size(min = 3, max = 250)
        String legalName,

        @NotBlank
        @Size(min = 3, max = 250)
        String tradeName,

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
