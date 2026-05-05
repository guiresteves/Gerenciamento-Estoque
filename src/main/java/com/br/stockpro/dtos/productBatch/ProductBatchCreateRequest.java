package com.br.stockpro.dtos.productBatch;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Schema(description = "Dados para registro de um novo lote. Ao criar o lote, a entrada no estoque é feita automaticamente")
public record ProductBatchCreateRequest(

        @Schema(description = "ID do produto — deve ter trackExpiration = true", example = "1")
        @NotNull(message = "O Id do produto é obrigatório")
        Long productId,

        @Schema(description = "Código do lote fornecido pelo fabricante — deve ser único por produto", example = "L2024001")
        @NotBlank(message = "O código do lote é obrigatório")
        @Size(max = 100, message = "O código do lote deve ter no máximo 100 caracteres")
        String batchCode,

        @Schema(description = "Data de validade do lote no formato ISO 8601 — deve ser uma data futura", example = "2025-12-31")
        @NotNull(message = "A data de validade é obrigatória")
        @Future(message = "A data de validade deve ser uma data futura")
        LocalDate expirationDate,

        @Schema(description = "Quantidade de unidades recebidas neste lote", example = "100")
        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser maior que zero")
        Integer quantity
) {}