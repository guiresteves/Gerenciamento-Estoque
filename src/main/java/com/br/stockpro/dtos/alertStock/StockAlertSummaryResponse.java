package com.br.stockpro.dtos.alertStock;

public record StockAlertSummaryResponse(
        long totalActive,
        long lowStock,
        long outOfStock,
        long longOutOfStock,
        long aboveMaximum
) {
}
