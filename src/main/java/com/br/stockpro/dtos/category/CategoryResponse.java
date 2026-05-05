package com.br.stockpro.dtos.category;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Dados da Categoria retornados pela API")
public record CategoryResponse(

        @Schema(description = "ID da Categoria", example = "1")
        Long id,

        @Schema(description = "Nome da Categoria", example = "Bebidas")
        String name,

        @Schema(description = "Descrição da Categoria", example = "Bebidas alcoólicas e não alcoólicas")
        String description,

        @Schema(description = "Status da Categoria", example = "true")
        Boolean active,

        @Schema(description = "Data de crição no formato ISO 8601", example = "2024-01-15T10:3000Z")
        Instant createdAt,

        @Schema(description = "Data da última atualização no formato ISO 8601", example = "2024-01-15T10:3000Z")
        Instant updatedAt

) {
}
