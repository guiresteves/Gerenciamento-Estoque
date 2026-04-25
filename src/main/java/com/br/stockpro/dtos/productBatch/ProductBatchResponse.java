package com.br.stockpro.dtos.productBatch;

import com.br.stockpro.enums.BatchStatus;

import java.time.Instant;
import java.time.LocalDate;

public record ProductBatchResponse(

        Long id,
        Long companyId,

        Long productId,
        String productName,
        String productBarCode,

        Long stockId,
        Long stockMovimentId,

        String bachCode,
        LocalDate expirationDate,
        Integer daysToExpiration,
        Integer quantity,
        Integer remainingQuantity,

        BatchStatus status,

        Boolean active,

        Instant createdAt,
        Instant updatedAt
) {
}
