package com.br.stockpro.dtos.stockMovement;

import com.br.stockpro.enums.MovementType;
import jakarta.validation.constraints.*;

public record StockMovementManualRequest(

        @NotNull(message = "O ID do produto é obrigatório")
        Long productId,

        @NotNull(message = "O tipo de movimento é obrigatório")
        MovementType movementType,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser maior que zero")
        Integer quantity,

        @Size(max = 255, message = "O motivo deve ter no máximo 255 caracteres")
        String reason

) {
}
