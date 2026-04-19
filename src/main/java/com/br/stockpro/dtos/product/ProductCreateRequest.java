package com.br.stockpro.dtos.product;

import com.br.stockpro.enums.UnitOfMeasure;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductCreateRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 250, message = "Nome deve ter entre 3 e 250 caracteres")
        String name,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String description,

        @NotBlank(message = "Código de barras é obrigatório")
        @Size(min = 8, max = 50, message = "O código de barras deve ter entre 8 e 50 caracteres")
        String barcode,

        @NotNull(message = "A unidade de medida é obrigatória")
        UnitOfMeasure unitOfMeasure,

        Boolean trackExpiration,

        @NotNull(message = "O preço de custo é obrigatório")
        @DecimalMin(value = "0.00", message = "O preço de custo deve ser maior ou igual a zero")
        @Digits(integer = 10, fraction = 2)
        BigDecimal costPrice,

        @NotNull(message = "O preço de venda é obrigatório")
        @DecimalMin(value = "0.00", message = "O preço de venda deve ser maior ou igual a zero")
        @Digits(integer = 10, fraction = 2)
        BigDecimal salePrice,

        @Min(value = 0, message = "Estoque mínimo deve ser maior ou igual a zero")
        Integer minStock,
        @Min(value = 0, message = "Estoque máximo deve ser maior ou igual a zero")
        Integer maxStock,

        @NotNull(message = "Categoria é obrigatória")
        Long categoryId
) {
}