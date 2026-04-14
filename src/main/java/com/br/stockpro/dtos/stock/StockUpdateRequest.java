package com.br.stockpro.dtos.stock;

import jakarta.validation.constraints.*;

public record StockUpdateRequest(

        @Min(value = 0, message = "A quantidade mínima não pode ser negativa")
        Integer minQuantity,

        @Size(max = 100, message = "A localização deve ter no máximo 100 caracteres")
        String location,

        Boolean active

) {
}
