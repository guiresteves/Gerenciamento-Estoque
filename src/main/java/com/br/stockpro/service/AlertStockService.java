package com.br.stockpro.service;

import com.br.stockpro.dtos.alertStock.AlertStockResponse;
import com.br.stockpro.dtos.alertStock.StockAlertSummaryResponse;
import com.br.stockpro.enums.AlertStatus;
import com.br.stockpro.enums.AlertType;
import com.br.stockpro.exceptions.BusinessException;
import com.br.stockpro.exceptions.NotFoundException;
import com.br.stockpro.mapper.AlertSotckMapper;
import com.br.stockpro.model.*;
import com.br.stockpro.repository.AlertStockRepository;
import com.br.stockpro.repository.StockRepository;
import com.br.stockpro.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertStockService {

    private final AlertStockRepository alertStockRepository;
    private final AlertSotckMapper alertSotckMapper;
    private final StockRepository stockRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final EmailService emailService;

    private static final int LONG_OUT_OF_STOCK_DAYS = 7;

    // chamado pelo scheduler e pelo StockService após cada movimentação
    @Transactional
    public void checkAndGenerateAlerts(Stock stock) {
        Company company = stock.getCompany();
        Product product = stock.getProduct();

        if (stock.getQuantity() == 0) {
            handleOutOfStockAlert(stock, company, product);

        } else if (stock.isAboveMaximum()) {
            // novo — alerta de excesso
            handleAboveMaximumAlert(stock, company, product);

        } else if (stock.isBelowMinimum()) {
            handleLowStockAlert(stock, company, product);
            resolveAlertIfExists(product.getId(), company.getId(), AlertType.OUT_OF_STOCK);
            resolveAlertIfExists(product.getId(), company.getId(), AlertType.LONG_OUT_OF_STOCK);

        } else {
            resolveAllActiveAlerts(product.getId(), company.getId());
        }
    }

    @Transactional
    public void checkLongOutOfStock(Stock stock) {
        Company company = stock.getCompany();
        Product product = stock.getProduct();

        if (stock.getQuantity() > 0) return;

        // verifica há quantos dias está zerado
        alertStockRepository.findByProductIdAndCompanyIdAndAlertTypeAndAlertStatus(
                        product.getId(), company.getId(),
                        AlertType.OUT_OF_STOCK, AlertStatus.ACTIVE)
                .ifPresent(alert -> {
                    long days = ChronoUnit.DAYS.between(alert.getCreatedAt(), Instant.now());

                    if (days >= LONG_OUT_OF_STOCK_DAYS) {
                        generateAlert(stock, company, product,
                                AlertType.LONG_OUT_OF_STOCK, (int) days);
                    }
                });
    }

    // consultas

    @Transactional(readOnly = true)
    public Page<AlertStockResponse> findAll(Pageable pageable) {
        Company company = getCurrentUserCompany();
        return alertStockRepository
                .findByCompanyIdOrderByCreatedAtDesc(company.getId(), pageable)
                .map(alertSotckMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AlertStockResponse> findByStatus(AlertStatus status, Pageable pageable) {
        Company company = getCurrentUserCompany();
        return alertStockRepository
                .findByCompanyIdAndAlertStatusOrderByCreatedAtDesc(company.getId(), status, pageable)
                .map(alertSotckMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AlertStockResponse> findByType(AlertType type, Pageable pageable) {
        Company company = getCurrentUserCompany();
        return alertStockRepository
                .findByCompanyIdAndAlertTypeOrderByCreatedAtDesc(company.getId(), type, pageable)
                .map(alertSotckMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public StockAlertSummaryResponse getSummary() {
        Company company = getCurrentUserCompany();
        Long companyId = company.getId();

        long totalActive = alertStockRepository
                .countByCompanyIdAndAlertStatus(companyId, AlertStatus.ACTIVE);
        long lowStock = alertStockRepository
                .countByCompanyIdAndAlertTypeAndAlertStatus(companyId, AlertType.LOW_STOCK, AlertStatus.ACTIVE);
        long outOfStock = alertStockRepository
                .countByCompanyIdAndAlertTypeAndAlertStatus(companyId, AlertType.OUT_OF_STOCK, AlertStatus.ACTIVE);
        long longOutOfStock = alertStockRepository
                .countByCompanyIdAndAlertTypeAndAlertStatus(companyId, AlertType.LONG_OUT_OF_STOCK, AlertStatus.ACTIVE);
        long aboveMaximum = alertStockRepository
                .countByCompanyIdAndAlertTypeAndAlertStatus(companyId, AlertType.ABOVE_MAXIMUM, AlertStatus.ACTIVE);

        return new StockAlertSummaryResponse(totalActive, lowStock, outOfStock, longOutOfStock, aboveMaximum);
    }

    // operador reconhece o alerta
    @Transactional
    public AlertStockResponse acknowledge(Long alertId) {
        Company company = getCurrentUserCompany();

        AlertStock alert = alertStockRepository.findById(alertId)
                .filter(a -> a.getCompany().getId().equals(company.getId()))
                .orElseThrow(() -> new NotFoundException("Alerta não encontrado"));

        if (alert.getAlertStatus() != AlertStatus.ACTIVE) {
            throw new BusinessException("Apenas alertas ativos podem ser reconhecidos");
        }

        alert.setAlertStatus(AlertStatus.ACKNOWLEDGED);
        alert.setAcknowledgedAt(Instant.now());

        return alertSotckMapper.toResponse(alertStockRepository.save(alert));
    }

    // métodos privados


    private void handleAboveMaximumAlert(Stock stock, Company company, Product product) {
        boolean alreadyExists = alertStockRepository
                .existsByProductIdAndCompanyIdAndAlertTypeAndAlertStatus(
                        product.getId(), company.getId(),
                        AlertType.ABOVE_MAXIMUM, AlertStatus.ACTIVE);

        if (!alreadyExists) {
            generateAlert(stock, company, product, AlertType.ABOVE_MAXIMUM, null);
        }
    }

    private void handleLowStockAlert(Stock stock, Company company, Product product) {
        boolean alreadyExists = alertStockRepository
                .existsByProductIdAndCompanyIdAndAlertTypeAndAlertStatus(
                        product.getId(), company.getId(),
                        AlertType.LOW_STOCK, AlertStatus.ACTIVE);

        if (!alreadyExists) {
            generateAlert(stock, company, product, AlertType.LOW_STOCK, null);
        }
    }

    private void handleOutOfStockAlert(Stock stock, Company company, Product product) {
        // resolve LOW_STOCK se existir — produto zerado é mais grave
        resolveAlertIfExists(product.getId(), company.getId(), AlertType.LOW_STOCK);

        boolean alreadyExists = alertStockRepository
                .existsByProductIdAndCompanyIdAndAlertTypeAndAlertStatus(
                        product.getId(), company.getId(),
                        AlertType.OUT_OF_STOCK, AlertStatus.ACTIVE);

        if (!alreadyExists) {
            generateAlert(stock, company, product, AlertType.OUT_OF_STOCK, null);
        }
    }

    private void generateAlert(Stock stock, Company company,
                               Product product, AlertType type, Integer daysOutOfStock) {

        AlertStock alert = AlertStock.builder()
                .company(company)
                .product(product)
                .stock(stock)
                .alertType(type)
                .alertStatus(AlertStatus.ACTIVE)
                .quantityAtAlert(stock.getQuantity())
                .minStockAtAlert(product.getMinStock())
                .maxStockAtAlert(product.getMaxStock())
                .daysOutOfStock(daysOutOfStock)
                .build();

        alertStockRepository.save(alert);

        log.info("Alerta gerado — tipo: {}, produto: {}, empresa: {}",
                type, product.getName(), company.getId());

        // notificar por email
        emailService.sendStockAlert(company, product, type,
                stock.getQuantity(), product.getMinStock());
    }

    private void resolveAlertIfExists(Long productId, Long companyId, AlertType type) {
        alertStockRepository.findByProductIdAndCompanyIdAndAlertTypeAndAlertStatus(
                        productId, companyId, type, AlertStatus.ACTIVE)
                .ifPresent(alert -> {
                    alert.setAlertStatus(AlertStatus.RESOLVED);
                    alert.setResolvedAt(Instant.now());
                    alertStockRepository.save(alert);
                });
    }

    private void resolveAllActiveAlerts(Long productId, Long companyId) {
        List<AlertStock> activeAlerts = alertStockRepository
                .findByProductIdAndCompanyIdAndAlertStatus(
                        productId, companyId, AlertStatus.ACTIVE);

        activeAlerts.forEach(alert -> {
            alert.setAlertStatus(AlertStatus.RESOLVED);
            alert.setResolvedAt(Instant.now());
        });

        alertStockRepository.saveAll(activeAlerts);
    }

    private Company getCurrentUserCompany() {
        User currentUser = authenticatedUserService.getCurrentUser();
        if (currentUser.getCompany() == null) {
            throw new BusinessException("Usuário não possui empresa vinculada");
        }
        return currentUser.getCompany();
    }

}
