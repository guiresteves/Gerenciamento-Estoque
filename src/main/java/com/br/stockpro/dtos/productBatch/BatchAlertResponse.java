package com.br.stockpro.dtos.productBatch;

import com.br.stockpro.enums.BatchStatus;

import java.time.LocalDate;

public record BatchAlertResponse(

        Long batchId,
        Long productId,
        String productName,
        String productBarcode,
        String batchCode,
        LocalDate expirationDate,
        Integer daysToExpiration,
        Integer remainingQuantity,
        BatchStatus status
) {
}
