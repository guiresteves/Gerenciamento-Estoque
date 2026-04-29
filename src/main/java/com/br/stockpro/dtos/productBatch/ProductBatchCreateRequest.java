package com.br.stockpro.dtos.productBatch;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ProductBatchCreateRequest (

        @NotNull(message = "O Id do produto é obrigatório")
        Long productId,

        @NotBlank(message = "O código do lote é obrigatório")
        @Size(max = 100, message = "O código do lote deve ter no máximo 100 caracteres")
        String batchCode,

        @NotNull(message = "A data de validade é obrigatória")
        @Future(message = "A data de validade deve ser uma data futura")
        LocalDate expirationDate,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser maior que zero")
        Integer quantity

) {
}
