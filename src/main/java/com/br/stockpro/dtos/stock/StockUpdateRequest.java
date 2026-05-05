package com.br.stockpro.dtos.stock;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados para atualização do estoque — todos os campos são opcionais")
public record StockUpdateRequest(

        @Schema(description = "Nova localização física do produto no estoque", example = "Corredor B - Prateleira 1")
        @Size(max = 100, message = "A localização deve ter no máximo 100 caracteres")
        String location,

        @Schema(description = "Status do estoque", example = "true")
        Boolean active
) {}