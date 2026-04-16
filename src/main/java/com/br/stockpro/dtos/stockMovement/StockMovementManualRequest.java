package com.br.stockpro.dtos.stockMovement;

import com.br.stockpro.enums.MovementType;

public record StockMovementManualRequest(

        Long productId,
        MovementType movementType,
        Integer quantity,
        String reason

) {
}
