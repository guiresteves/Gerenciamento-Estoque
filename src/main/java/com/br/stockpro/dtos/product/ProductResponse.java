package com.br.stockpro.dtos.product;

import com.br.stockpro.enums.UnitOfMeasure;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(

        Long id,
        String name,
        String description,
        String barcode,
        UnitOfMeasure unitOfMeasure,
        Boolean trackExpiration,
        Boolean active,
        BigDecimal costPrice,
        BigDecimal salePrice,

        Integer minStock,
        Integer maxStock,

        Long companyId,
        String companyLegalName,

        Long categoryId,
        String categoryName,

        Instant createdAt,
        Instant updatedAt
) {
}
