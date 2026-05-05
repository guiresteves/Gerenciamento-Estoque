package com.br.stockpro.dtos.product;

import com.br.stockpro.enums.UnitOfMeasure;
import io.swagger.v3.oas.annotations.media.DependentSchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Dados para a atualização de um produto, todos os campos são opcionais")
public record ProductUpdateRequest(

        @Schema(description = "Novo nome do produto", example = "Feijão Carioca")
        @Size(min = 3, max = 250, message = "Nome deve ter entre 3 e 250 caracteres")
        String name,

        @Schema(description = "Nova descrição de um produto", example = "Feijão Carioca de 5kg")
        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String description,

        @Schema(description = "Novo código de barras do produto", example = "12345678912300")
        @Size(min = 8, max = 50, message = "O código de barras deve ter entre 8 e 50 caracteres")
        String barcode,

        @Schema(description = "Nova unidade de medida do produto", example = "UNIDADE",
                allowableValues = {"UNIDADE", "KG", "G", "ML", "L", "CX", "PCT"})
        UnitOfMeasure unitOfMeasure,

        @Schema(description = "Indica se o produto terá o controle de validade", example = "true")
        Boolean trackExpiration,

        @Schema(description = "Novo valor de custo do produto", example = "11.90")
        @DecimalMin(value = "0.00", message = "O preço de custo deve ser maior ou igual a zero")
        @Digits(integer = 10, fraction = 2)
        BigDecimal costPrice,

        @Schema(description = "Novo valor de vendas do produto", example = "19.90")
        @DecimalMin(value = "0.00", message = "O preço de venda deve ser maior ou igual a zero")
        @Digits(integer = 10, fraction = 2)
        BigDecimal salePrice,

        @Schema(description = "Nova quantidade mínima do produto no estoque", example = "25")
        @Min(value = 0, message = "Estoque mínimo deve ser maior ou igual a zero")
        Integer minStock,

        @Schema(description = "Nova quantidade máxima do produto no estoque", example = "120")
        @Min(value = 0, message = "Estoque máximo deve ser maior ou igual a zero")
        Integer maxStock,

        @Schema(description = "Novo ID da categoria do produto", example = "1")
        Long categoryId
) {
}
