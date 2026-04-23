package com.br.stockpro.dtos.stock;

import jakarta.validation.constraints.*;

public record StockUpdateRequest(

        @Size(max = 100, message = "A localização deve ter no máximo 100 caracteres")
        String location,

        Boolean active

) {
}
