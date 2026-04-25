package com.br.stockpro.dtos.stockAlert;

public record StockAlertSummaryResponse(
        long totalActive,
        long lowStock,
        long outOfStock,
        long longOutOfStock,
        long aboveMaximum
) {
}
