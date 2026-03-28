package com.br.stockpro.dtos.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreateRequest(

        @Size(min = 3, max = 50, message = "Nome deve ter entre 3 a 50 caracteres")
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @Size(max = 500, message = "Nome deve ter no max 500 caracteres")
        String description
) {
}
