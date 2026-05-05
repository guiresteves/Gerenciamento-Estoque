package com.br.stockpro.dtos.stockAlert;

import com.br.stockpro.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "Dados do alerta de estoque retornados pela API")
public record StockAlertResponse(

        @Schema(description = "ID do alerta", example = "1")
        Long id,

        @Schema(description = "ID da empresa", example = "1")
        Long companyId,

        @Schema(description = "ID do produto que gerou o alerta", example = "1")
        Long productId,

        @Schema(description = "Nome do produto", example = "Coca-Cola 2L")
        String productName,

        @Schema(description = "Código de barras do produto", example = "7891234560001")
        String productBarcode,

        @Schema(description = "ID do estoque vinculado ao alerta", example = "1")
        Long stockId,

        @Schema(description = """
                Tipo do alerta:
                - `LOW_STOCK` → quantidade abaixo do mínimo definido no produto
                - `OUT_OF_STOCK` → produto zerado
                - `LONG_OUT_OF_STOCK` → produto zerado há mais de 7 dias
                - `ABOVE_MAXIMUM` → quantidade acima do máximo definido no produto
                """,
                allowableValues = {"LOW_STOCK", "OUT_OF_STOCK", "LONG_OUT_OF_STOCK", "ABOVE_MAXIMUM"},
                example = "LOW_STOCK")
        AlertType alertType,

        @Schema(description = """
                Status do alerta:
                - `ACTIVE` → alerta ativo, ainda não resolvido
                - `RESOLVED` → estoque normalizado, resolvido automaticamente
                - `ACKNOWLEDGED` → operador reconheceu o alerta
                """,
                allowableValues = {"ACTIVE", "RESOLVED", "ACKNOWLEDGED"},
                example = "ACTIVE")
        AlertStatus status,

        @Schema(description = "Quantidade em estoque no momento em que o alerta foi gerado", example = "5")
        Integer quantityAtAlert,

        @Schema(description = "Estoque mínimo definido no produto no momento do alerta", example = "10")
        Integer minQuantityAtAlert,

        @Schema(description = "Dias sem estoque — preenchido apenas para alertas do tipo LONG_OUT_OF_STOCK",
                example = "8", nullable = true)
        Integer daysOutOfStock,

        @Schema(description = "Data em que o alerta foi gerado no formato ISO 8601", example = "2024-01-15T08:00:00Z")
        Instant createdAt,

        @Schema(description = "Data em que o estoque foi normalizado e o alerta resolvido automaticamente. Null se ainda ativo",
                example = "2024-01-16T10:00:00Z", nullable = true)
        Instant resolvedAt,

        @Schema(description = "Data em que o operador reconheceu o alerta. Null se ainda não reconhecido",
                example = "2024-01-15T09:00:00Z", nullable = true)
        Instant acknowledgedAt
) {}