package com.br.stockpro.dtos.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualizações de um Categoria - Todos os campos são opcionais")
public record CategoryUpdateRequest(

        @Schema(description = "Novo nome de uma Categoria", example = "Bebidas e Sucos")
        @Size(min = 3, max = 50, message = "Nome deve ter entre 3 a 50 caracteres")
        String name,

        @Schema(description = "Nova descrição da Categoria", example = "Bevidas em geral incluido sucos naturais")
        @Size(max = 500, message = "Nome deve ter no max 500 caracteres")
        String description,

        @Schema(description = "Novo Status de uma Categoria")
        Boolean active
) {
}
