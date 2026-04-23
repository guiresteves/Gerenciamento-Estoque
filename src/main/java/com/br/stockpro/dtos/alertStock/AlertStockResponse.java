package com.br.stockpro.dtos.alertStock;

import com.br.stockpro.enums.*;

import java.time.Instant;

public record AlertStockResponse(
        Long id,
        Long companyId,
        Long productId,
        String productName,
        String productBarcode,
        Long stockId,
        AlertType alertType,
        AlertStatus status,
        Integer quantityAtAlert,
        Integer minQuantityAtAlert,
        Integer daysOutOfStock,
        Instant createdAt,
        Instant resolvedAt,
        Instant acknowledgedAt
) {
}
