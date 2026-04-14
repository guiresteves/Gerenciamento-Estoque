package com.br.stockpro.dtos.stock;

import com.br.stockpro.enums.UnitOfMeasure;

import java.math.BigDecimal;
import java.time.Instant;

public record StockResponse(

        Long id,
        Long companyId,
        Long productId,
        String productName,
        String productBarcode,
        UnitOfMeasure unitOfMeasure,

        Integer quantity,
        Integer reservedQuantity,
        Integer availableQuantity,

        Integer minQuantity,

        String location,
        Boolean active,
        Boolean belowMinimum,

        Instant createdAt,
        Instant updatedAt

) {
}
