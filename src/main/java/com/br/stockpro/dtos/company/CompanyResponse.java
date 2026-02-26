package com.br.stockpro.dtos.company;

import com.br.stockpro.enums.CompanyStatus;
import com.br.stockpro.enums.CompanyType;

import java.time.Instant;

public record CompanyResponse(

        Long id,
        String legalName,
        String tradeName,
        String cnpj,
        String stateRegistration,
        String municipalRegistration,
        CompanyType companyType,

        String phone,
        String email,

        CompanyStatus companyStatus,

        Instant createdAt,
        Instant updatedAt
) {
}
