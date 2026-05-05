package com.br.stockpro.dtos.product;

import com.br.stockpro.enums.UnitOfMeasure;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

@Schema(description = "Dados de um produto retornado pela API")
public record ProductResponse(

        @Schema(description = "ID do produto", example = "1")
        Long id,

        @Schema(description = "Nome do produto", example = "Arroz Branco")
        String name,

        @Schema(description = "Descrição do produto", example = "Arroz Branco de 5Kg")
        String description,

        @Schema(description = "Código de barras do produto", example = "78912345678900")
        String barcode,

        @Schema(description = "Unidade de medida do produto", example = "UNIDADE",
                allowableValues = {"UNIDADE", "KG", "G", "ML", "L", "CX", "PCT"})
        UnitOfMeasure unitOfMeasure,

        @Schema(description = "Inica se o produto terá o controle de validade", example = "true")
        Boolean trackExpiration,

        @Schema(description = "Indica se o produto esta ativo", example = "true")
        Boolean active,

        @Schema(description = "Precço de custo do produto", example = "10.90")
        BigDecimal costPrice,

        @Schema(description = "Preço de venda do produto", example = "15.90")
        BigDecimal salePrice,

        @Schema(description = "Quantidade mínima do produto no estoque", example = "10")
        Integer minStock,

        @Schema(description = "Quantidade máxima do produto no estoque", example = "100")
        Integer maxStock,

        @Schema(description = "ID da empresa", example = "1")
        Long companyId,

        @Schema(description = "Nome da empresa", example = "Empresa Tecnologia LTDA")
        String companyLegalName,

        @Schema(description = "ID da categoria do produto", example = "1")
        Long categoryId,

        @Schema(description = "Nome da categoria do produto", example = "Alimentício")
        String categoryName,

        @Schema(description = "Data de crição no formato ISO 8601", example = "2024-01-15T10:3000Z")
        Instant createdAt,

        @Schema(description = "Data da última atualização no formato ISO 8601", example = "2024-01-15T10:3000Z")
        Instant updatedAt
) {
}
