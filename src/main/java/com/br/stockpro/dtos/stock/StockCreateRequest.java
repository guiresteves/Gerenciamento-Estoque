package com.br.stockpro.dtos.stock;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados para criação do estoque de um produto")
public record StockCreateRequest(

        @Schema(description = "ID do produto — deve estar ativo e não possuir estoque cadastrado", example = "1")
        @NotNull(message = "O ID do produto é obrigatório")
        Long productId,

        @Schema(description = """
                Quantidade inicial em estoque.
                Para produtos com controle de validade (trackExpiration = true),
                prefira registrar a entrada via /api/batches informando o lote e a validade.
                """, example = "50")
        @Min(value = 0, message = "A quantidade não pode ser negativa")
        Integer quantity,

        @Schema(description = "Localização física do produto no estoque", example = "Corredor A - Prateleira 3")
        @Size(max = 100, message = "A localização deve ter no máximo 100 caracteres")
        String location
) {}