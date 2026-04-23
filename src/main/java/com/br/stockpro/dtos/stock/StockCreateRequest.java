package com.br.stockpro.dtos.stock;

import jakarta.validation.constraints.*;

public record StockCreateRequest(

        @NotNull(message = "O ID do produto é obrigatório")
        Long productId,

        @Min(value = 0, message = "A quantidade não pode ser negativa")
        Integer quantity,

        @Size(max = 100, message = "A localização deve ter no máximo 100 caracteres")
        String location
) {
}
