package com.br.stockpro.dtos.product;

import com.br.stockpro.enums.UnitOfMeasure;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import javax.crypto.spec.DESedeKeySpec;
import java.math.BigDecimal;

@Schema(description = "Dados para a criação de um novo produto")
public record ProductCreateRequest(

        @Schema(description = "Nome do produto", example = "Arroz Branco",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 250, message = "Nome deve ter entre 3 e 250 caracteres")
        String name,

        @Schema(description = "Descrição de um produto", example = "Pacote de Arroz Branco de 5kg",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String description,

        @Schema(description = "Código de barras do produto", example = "78912345678900",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Código de barras é obrigatório")
        @Size(min = 8, max = 50, message = "O código de barras deve ter entre 8 e 50 caracteres")
        String barcode,

        @Schema(description = "Unidade de medida do produto", example = "UNIDADE",
                allowableValues = {"UNIDADE", "KG", "G", "ML", "L", "CX", "PCT"},
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "A unidade de medida é obrigatória")
        UnitOfMeasure unitOfMeasure,

        @Schema(description = "Indica se o produto terá o controle de valide", example = "true",
                requiredMode = Schema.RequiredMode.REQUIRED)
        Boolean trackExpiration,

        @Schema(description = "Preço de custo do produto", example = "10.50",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O preço de custo é obrigatório")
        @DecimalMin(value = "0.00", message = "O preço de custo deve ser maior ou igual a zero")
        @Digits(integer = 10, fraction = 2)
        BigDecimal costPrice,

        @Schema(description = "Preço de venda do prdoduto", example = "15.90",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O preço de venda é obrigatório")
        @DecimalMin(value = "0.00", message = "O preço de venda deve ser maior ou igual a zero")
        @Digits(integer = 10, fraction = 2)
        BigDecimal salePrice,

        @Schema(description = "Quantidade mínima do produto no estoque", example = "10",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Min(value = 0, message = "Estoque mínimo deve ser maior ou igual a zero")
        Integer minStock,

        @Schema(description = "Quantiade máxima do prosuto no estoque", example = "100",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Min(value = 0, message = "Estoque máximo deve ser maior ou igual a zero")
        Integer maxStock,

        @Schema(description = "ID da categoria do produto", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Categoria é obrigatória")
        Long categoryId
) {
}