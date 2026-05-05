package com.br.stockpro.dtos.productBatch;

import com.br.stockpro.enums.BatchStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.time.LocalDate;

@Schema(description = "Dados do lote retornados pela API")
public record ProductBatchResponse(

        @Schema(description = "ID do lote", example = "1")
        Long id,

        @Schema(description = "ID da empresa", example = "1")
        Long companyId,

        @Schema(description = "ID do produto", example = "1")
        Long productId,

        @Schema(description = "Nome do produto", example = "Coca-Cola 2L")
        String productName,

        @Schema(description = "Código de barras do produto", example = "7891234560001")
        String productBarcode, // corrigido

        @Schema(description = "ID do estoque vinculado", example = "1")
        Long stockId,

        @Schema(description = "ID do movimento de entrada gerado automaticamente", example = "1")
        Long stockMovementId,

        @Schema(description = "Código do lote", example = "L2024001")
        String batchCode, // corrigido

        @Schema(description = "Data de vencimento do lote", example = "2025-12-31")
        LocalDate expirationDate,

        @Schema(description = "Dias restantes para o vencimento. Negativo indica lote já vencido", example = "180")
        Integer daysToExpiration,

        @Schema(description = "Quantidade total recebida no lote", example = "100")
        Integer quantity,

        @Schema(description = "Quantidade restante disponível no lote", example = "75")
        Integer remainingQuantity,

        @Schema(description = "Status do lote",
                allowableValues = {"ACTIVE", "EXPIRING", "EXPIRED"},
                example = "ACTIVE")
        BatchStatus status,

        @Schema(description = "Se o lote está ativo no sistema", example = "true")
        Boolean active,

        @Schema(description = "Data de criação no formato ISO 8601", example = "2024-01-15T10:30:00Z")
        Instant createdAt,

        @Schema(description = "Data da última atualização no formato ISO 8601", example = "2024-01-15T10:30:00Z")
        Instant updatedAt
) {}