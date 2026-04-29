package com.br.stockpro.service;


import com.br.stockpro.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductBatchScheduler {

    private final ProductBatchService productBatchService;
    private final CompanyRepository companyRepository;

    // todo dia às 6h — atualiza status e days_to_expiration de todos os lotes
    @Scheduled(cron = "0 0 6 * * *")
    public void updateBatchStatuses() {
        log.info("Iniciando atualização de status dos lotes");

        companyRepository.findAllActiveCompanies().forEach(company -> {
            try {
                productBatchService.updateAllBatchStatuses(company.getId());
            } catch (Exception e) {
                log.error("Erro ao atualizar lotes da empresa {}: {}",
                        company.getId(), e.getMessage());
            }
        });

        log.info("Atualização de status concluída");
    }

    // todo dia às 6h30 — baixa automática de produtos vencidos
    @Scheduled(cron = "0 30 6 * * *")
    public void processExpiredBatches() {
        log.info("Iniciando baixa automática de lotes vencidos");
        productBatchService.processExpiredBatches();
        log.info("Baixa automática concluída");
    }
}
