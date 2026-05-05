package com.br.stockpro.dtos.stock;

import com.br.stockpro.enums.UnitOfMeasure;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "Dados do estoque retornados pela API")
public record StockResponse(

        @Schema(description = "ID do estoque", example = "1")
        Long id,

        @Schema(description = "ID da empresa", example = "1")
        Long companyId,

        @Schema(description = "ID do produto", example = "1")
        Long productId,

        @Schema(description = "Nome do produto", example = "Coca-Cola 2L")
        String productName,

        @Schema(description = "Código de barras do produto", example = "7891234560001")
        String productBarcode,

        @Schema(description = "Unidade de medida do produto",
                allowableValues = {"UNIDADE", "KG", "G", "L", "ML", "CX", "PCT"},
                example = "UNIDADE")
        UnitOfMeasure unitOfMeasure,

        @Schema(description = "Quantidade total em estoque", example = "50")
        Integer quantity,

        @Schema(description = "Quantidade reservada — não disponível para venda", example = "10")
        Integer reservedQuantity,

        @Schema(description = "Quantidade disponível para venda — calculada como quantity - reservedQuantity", example = "40")
        Integer availableQuantity,

        @Schema(description = "Estoque mínimo definido no cadastro do produto — abaixo disso gera alerta LOW_STOCK", example = "10")
        Integer minStock,

        @Schema(description = "Estoque máximo definido no cadastro do produto — acima disso gera alerta ABOVE_MAXIMUM", example = "100")
        Integer maxStock,

        @Schema(description = "Indica se a quantidade atual está acima do máximo definido", example = "false")
        Boolean aboveMaximum,

        @Schema(description = "Indica se a quantidade atual está abaixo do mínimo definido", example = "false")
        Boolean belowMinimum,

        @Schema(description = "Localização física do produto no estoque", example = "Corredor A - Prateleira 3")
        String location,

        @Schema(description = "Status do estoque", example = "true")
        Boolean active,

        @Schema(description = "Data de criação no formato ISO 8601", example = "2024-01-15T10:30:00Z")
        Instant createdAt,

        @Schema(description = "Data da última atualização no formato ISO 8601", example = "2024-01-15T10:30:00Z")
        Instant updatedAt
) {}