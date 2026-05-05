package com.br.stockpro.dtos.productBatch;

import com.br.stockpro.enums.BatchStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Açerat de lote proximo ao vencimento ou já vencidos")
public record BatchAlertResponse (

        @Schema(description = "ID do lote", example = "1")
        Long batchId,

        @Schema(description = "ID do produto", example = "1")
        Long productId,

        @Schema(description = "Nome do produto", example = "Coca-Cola 2L")
        String productName,

        @Schema(description = "Código de barras do produto", example = "7891234560001")
        String productBarcode,

        @Schema(description = "Código do lote", example = "L2024001")
        String batchCode,

        @Schema(description = "Data de vencimento do lote", example = "2025-01-15")
        LocalDate expirationDate,

        @Schema(description = """
                Dias restantes para o vencimento
                - Negativo -> Já vencido;
                - 0 a 7 -> crítico;
                - 8 a 15 -> atenção;
                - 16 a 30 -> aviso
                """, example = "7")
        Integer daysToExpiration,

        @Schema(description = "Quantidade restante do Lote", example = "30")
        Integer remainingQuantity,

        @Schema(description = "Status do lote", allowableValues = {"ACTIVE", "EXPIRING", "EXPIRED"},
                example = "EXPIRING")
        BatchStatus status

) {
}
