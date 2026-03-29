package com.br.stockpro.dtos.supplier;

import java.time.Instant;

public record SupplierResponse(

        Long id,
        String legalName,
        String tradeName,
        String cnpj,
        String stateRegistration,
        String municipalRegistration,

        String phone,
        String email,

        Boolean active,

        Instant createdAt,
        Instant updatedAt
) {
}
