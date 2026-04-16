package com.br.stockpro.dtos.stockMovement;

import com.br.stockpro.enums.MovementOrigin;
import com.br.stockpro.enums.MovementType;

import java.time.Instant;

public record StockMovementResponse(

        Long id,
        Long companyId,
        Long stockId,
        Long productId,

        String productName,
        String productBarCode,

        String performedByName,

        MovementType movementType,
        MovementOrigin movementOrigin,

        Integer quantity,
        Integer previousQuantity,
        Integer currentQuantity,

        String reason,

        Long referenceId,
        String referenceType,

        Instant createdAt
) {
}
