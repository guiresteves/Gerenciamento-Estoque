package com.br.stockpro.dtos.product;

import com.br.stockpro.enums.UnitOfMeasure;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductUpdateRequest(

        @Size(min = 3, max = 250, message = "Nome deve ter entre 3 e 250 caracteres")
        String name,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String description,

        @Size(min = 8, max = 50, message = "O código de barras deve ter entre 8 e 50 caracteres")
        String barcode,

        UnitOfMeasure unitOfMeasure,

        Boolean trackExpiration,

        @DecimalMin(value = "0.00", message = "O preço de custo deve ser maior ou igual a zero")
        @Digits(integer = 10, fraction = 2)
        BigDecimal costPrice,

        @DecimalMin(value = "0.00", message = "O preço de venda deve ser maior ou igual a zero")
        @Digits(integer = 10, fraction = 2)
        BigDecimal salePrice,

        Long categoryId
) {
}
