package com.br.stockpro.dtos.stock;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record StockCreateRequest(

        @NotNull(message = "O ID do produto é obrigatório")
        Long productId,

        @Min(value = 0, message = "A quantidade não pode ser negativa")
        Integer quantity,

        @Min(value = 0, message = "A quantidade reservada não pode ser negativa")
        Integer reservedQuantity,

        @Min(value = 0, message = "A quantidade mínima não pode ser negativa")
        Integer minQuantity,

        @Size(max = 100, message = "A localização deve ter no máximo 100 caracteres")
        String location,

        Boolean active

) {
}
