package com.br.stockpro.dtos.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para um criação de uma nova Categoria")
public record CategoryCreateRequest(

        @Schema(description = "Nome da categoria deve ser único", example = "Bebidas")
        @Size(min = 3, max = 50, message = "Nome deve ter entre 3 a 50 caracteres")
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @Schema(description = "Descrição detalhada da Categoria", example = "Bebidas alcoólicas e não alcoólicas")
        @Size(max = 500, message = "Nome deve ter no max 500 caracteres")
        String description
) {
}
