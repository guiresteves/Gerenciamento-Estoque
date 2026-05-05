package com.br.stockpro.dtos.stockMovement;

import com.br.stockpro.enums.MovementOrigin;
import com.br.stockpro.enums.MovementType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "Dados da movimentação de estoque retornados pela API")
public record StockMovementResponse(

        @Schema(description = "ID da movimentação", example = "1")
        Long id,

        @Schema(description = "ID da empresa", example = "1")
        Long companyId,

        @Schema(description = "ID do estoque movimentado", example = "1")
        Long stockId,

        @Schema(description = "ID do produto movimentado", example = "1")
        Long productId,

        @Schema(description = "Nome do produto", example = "Coca-Cola 2L")
        String productName,

        @Schema(description = "Código de barras do produto", example = "7891234560001")
        String productBarcode, // corrigido

        @Schema(description = "Nome do usuário que realizou a movimentação. Null em operações automáticas do sistema",
                example = "João Silva", nullable = true)
        String performedByName,

        @Schema(description = """
                Tipo da movimentação:
                - `ENTRADA` → entrada de mercadoria
                - `SAIDA` → saída por venda
                - `AJUSTE_POSITIVO` → ajuste de inventário para mais
                - `AJUSTE_NEGATIVO` → ajuste de inventário para menos
                - `RESERVA` → reserva de quantidade
                - `LIBERACAO` → liberação de reserva
                - `VENCIMENTO` → baixa por produto vencido
                - `DEVOLUCAO` → devolução de cliente
                """,
                allowableValues = {"ENTRADA", "SAIDA", "AJUSTE_POSITIVO", "AJUSTE_NEGATIVO",
                        "RESERVA", "LIBERACAO", "VENCIMENTO", "DEVOLUCAO"},
                example = "ENTRADA")
        MovementType movementType,

        @Schema(description = """
                Origem da movimentação:
                - `MANUAL` → registrada por um usuário
                - `SYSTEM` → gerada automaticamente pelo sistema
                """,
                allowableValues = {"MANUAL", "SYSTEM"},
                example = "MANUAL")
        MovementOrigin movementOrigin,

        @Schema(description = "Quantidade movimentada", example = "10")
        Integer quantity,

        @Schema(description = "Quantidade em estoque antes da movimentação", example = "50")
        Integer previousQuantity,

        @Schema(description = "Quantidade em estoque após a movimentação", example = "60")
        Integer currentQuantity,

        @Schema(description = "Motivo da movimentação", example = "Recebimento de mercadoria — NF 12345",
                nullable = true)
        String reason,

        @Schema(description = "ID de referência externa — usado para rastrear movimentos gerados por outros módulos",
                example = "1", nullable = true)
        Long referenceId,

        @Schema(description = "Tipo da referência externa", example = "ORDER", nullable = true)
        String referenceType,

        @Schema(description = "Data em que a movimentação foi registrada no formato ISO 8601",
                example = "2024-01-15T10:30:00Z")
        Instant createdAt
) {}