package com.br.stockpro.dtos.stockMovement;

import com.br.stockpro.enums.MovementType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados para registro de uma movimentação manual de estoque")
public record StockMovementManualRequest(

        @Schema(description = "ID do produto a ser movimentado", example = "1")
        @NotNull(message = "O ID do produto é obrigatório")
        Long productId,

        @Schema(description = """
                Tipo do movimento manual permitido:
                - `ENTRADA` → entrada de mercadoria
                - `AJUSTE_POSITIVO` → correção de inventário para mais
                - `AJUSTE_NEGATIVO` → correção de inventário para menos
                - `DEVOLUCAO` → devolução de cliente
                - `VENCIMENTO` → baixa por produto vencido
                
                Tipos NÃO permitidos manualmente:
                - `SAIDA` → use /api/stocks/product/{id}/remove
                - `RESERVA` → use /api/stocks/product/{id}/reserve
                - `LIBERACAO` → use /api/stocks/product/{id}/release-reservation
                """,
                allowableValues = {"ENTRADA", "AJUSTE_POSITIVO", "AJUSTE_NEGATIVO", "DEVOLUCAO", "VENCIMENTO"},
                example = "ENTRADA")
        @NotNull(message = "O tipo de movimento é obrigatório")
        MovementType movementType,

        @Schema(description = "Quantidade a ser movimentada", example = "10")
        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser maior que zero")
        Integer quantity,

        @Schema(description = "Motivo da movimentação — recomendado para rastreabilidade",
                example = "Recebimento de mercadoria — NF 12345")
        @Size(max = 255, message = "O motivo deve ter no máximo 255 caracteres")
        String reason
) {}