package com.br.stockpro.service;

import com.br.stockpro.model.Stock;
import com.br.stockpro.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockAlertScheduler {

    private final StockRepository stockRepository;
    private final AlertStockService stockAlertService;

    // roda todo dia às 8h — verifica todos os estoques
    @Scheduled(cron = "0 0 8 * * *")
    public void checkAllStocks() {
        log.info("Iniciando verificação de alertas de estoque");

        List<Stock> allActiveStocks = stockRepository.findAllActiveStocks();

        allActiveStocks.forEach(stock -> {
            try {
                stockAlertService.checkAndGenerateAlerts(stock);
                stockAlertService.checkLongOutOfStock(stock);
            } catch (Exception e) {
                log.error("Erro ao verificar alerta para stock {}: {}",
                        stock.getId(), e.getMessage());
            }
        });

        log.info("Verificação concluída — {} estoques verificados", allActiveStocks.size());
    }
}
