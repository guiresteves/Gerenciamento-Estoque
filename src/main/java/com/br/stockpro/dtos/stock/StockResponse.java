package com.br.stockpro.dtos.stock;

import java.math.BigDecimal;
import java.time.Instant;

public record StockResponse(

        Long id,
        Long companyId,
        Long productId,
        String productName,
        String productBarcode,

        Integer quantity,
        Integer reservedQuantity,
        Integer availableQuantity,

        Integer minQuantity,

        BigDecimal averageCost,
        String location,
        Boolean active,
        Boolean belowMinimum,

        Instant createdAt,
        Instant updatedAt

) {
}
