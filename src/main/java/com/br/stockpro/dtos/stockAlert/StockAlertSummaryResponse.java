package com.br.stockpro.dtos.stockAlert;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = """
        Resumo dos alertas ativos agrupados por tipo.
        Ideal para alimentar o dashboard do sistema com uma única chamada à API.
        """)
public record StockAlertSummaryResponse(

        @Schema(description = "Total de alertas ativos independente do tipo", example = "12")
        long totalActive,

        @Schema(description = "Alertas ativos do tipo LOW_STOCK — quantidade abaixo do mínimo", example = "7")
        long lowStock,

        @Schema(description = "Alertas ativos do tipo OUT_OF_STOCK — produtos zerados", example = "3")
        long outOfStock,

        @Schema(description = "Alertas ativos do tipo LONG_OUT_OF_STOCK — zerados há mais de 7 dias", example = "1")
        long longOutOfStock,

        @Schema(description = "Alertas ativos do tipo ABOVE_MAXIMUM — quantidade acima do máximo", example = "1")
        long aboveMaximum
) {}