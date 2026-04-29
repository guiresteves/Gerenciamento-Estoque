package com.br.stockpro.service;


import com.br.stockpro.dtos.productBatch.BatchAlertResponse;
import com.br.stockpro.dtos.productBatch.ProductBatchCreateRequest;
import com.br.stockpro.dtos.productBatch.ProductBatchResponse;
import com.br.stockpro.enums.BatchStatus;
import com.br.stockpro.enums.MovementType;
import com.br.stockpro.exceptions.BusinessException;
import com.br.stockpro.exceptions.NotFoundException;
import com.br.stockpro.mapper.ProductBatchMapper;
import com.br.stockpro.model.*;
import com.br.stockpro.repository.ProductBatchRepository;
import com.br.stockpro.repository.ProductRepository;
import com.br.stockpro.repository.StockRepository;
import com.br.stockpro.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductBatchService {

    private final ProductBatchRepository productBatchRepository;
    private final ProductBatchMapper productBatchMapper;
    private final AuthenticatedUserService authenticatedUserService;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final StockMovementService stockMovementService;


    private static final int ALERT_30_DAYS = 30;
    private static final int ALERT_15_DAYS = 15;
    private static final int ALERT_7_DAYS = 7;

    @Transactional
    public ProductBatchResponse createBatch(ProductBatchCreateRequest request) {
        User currentUser = authenticatedUserService.getCurrentUser();
        Company company = getCurrentUserCompany();

        Product product = productRepository.findByIdAndCompanyId(request.productId(), company.getId())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));


        if (!Boolean.TRUE.equals(product.getTrackExpiration())) {
            throw new BusinessException(
                    "Este produto não possui o controle de validade ativo " +
                    "Ativo o campo de TarckExpiration no campo de Cadastro do Produto"
            );
        }

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new BusinessException("Não é possivel criar lote para produtos inativos");
        }

        if (productBatchRepository.existsByBatchCodeAndProductIdAndCompanyId(request.batchCode(), request.productId(), company.getId())) {
            throw new BusinessException("Já existe um lote com esse código para esse produto");
        }

        Stock stock = stockRepository.findByProductIdAndCompanyIdAndActiveTrue(request.productId(), company.getId())
                .orElseThrow( () -> new NotFoundException("Estoque não encontrado para o produto. Crie o estoque antes de registrar o lote"));

        Integer previousQuantity = stock.getQuantity();
        stock.setQuantity(stock.getQuantity() + request.quantity());
        stockRepository.save(stock);

        StockMovement movement = stockMovementService.registerStockMovementAndReturn(
                stock,
                currentUser,
                MovementType.ENTRADA,
                request.quantity(),
                previousQuantity,
                "Entrada por lote: " + request.batchCode()
        );

        ProductBatch batch = ProductBatch.builder()
                .company(company)
                .product(product)
                .stock(stock)
                .stockMovement(movement)
                .batchCode(request.batchCode())
                .expirationDate(request.expirationDate())
                .quantity(request.quantity())
                .remainingQuantity(request.quantity())
                .status(BatchStatus.ACTIVE)
                .build();


        batch.updateDaysToExpiration();
        updateBatchStatus(batch);

        return productBatchMapper.toResponse(productBatchRepository.save(batch));
    }

    @Transactional(readOnly = true)
    public Page<ProductBatchResponse> findAll(Pageable pageable) {
        Company company = getCurrentUserCompany();
        return productBatchRepository
                .findByCompanyIdOrderByExpirationDateAsc(company.getId(), pageable)
                .map(productBatchMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductBatchResponse> findByProduct(Long productId, Pageable pageable) {
        Company company = getCurrentUserCompany();
        return productBatchRepository
                .findByProductIdAndCompanyIdOrderByExpirationDateAsc(
                        productId, company.getId(), pageable)
                .map(productBatchMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductBatchResponse> findByStatus(BatchStatus status, Pageable pageable) {
        Company company = getCurrentUserCompany();
        return productBatchRepository
                .findByCompanyIdAndStatusOrderByExpirationDateAsc(
                        company.getId(), status, pageable)
                .map(productBatchMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ProductBatchResponse findById(Long id) {
        Company company = getCurrentUserCompany();
        return productBatchMapper.toResponse(getBatchOrThrow(id, company.getId()));
    }

    // retorna lotes próximos ao vencimento agrupados por urgência
    @Transactional(readOnly = true)
    public List<BatchAlertResponse> findExpiring() {
        Company company = getCurrentUserCompany();
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(ALERT_30_DAYS);

        return productBatchRepository.findExpiringBatches(company.getId(), today, limit)
                .stream()
                .map(productBatchMapper::toAlertResponse)
                .toList();
    }

    // chamado pelo scheduler — atualiza status de todos os lotes
    @Transactional
    public void updateAllBatchStatuses(Long companyId) {
        List<ProductBatch> batches = productBatchRepository
                .findAllActiveBatchesByCompany(companyId);

        batches.forEach(batch -> {
            batch.updateDaysToExpiration();
            updateBatchStatus(batch);
        });

        productBatchRepository.saveAll(batches);
    }

    // chamado pelo scheduler — baixa automática de vencidos
    @Transactional
    public void processExpiredBatches() {
        List<ProductBatch> expired = productBatchRepository
                .findExpiredNotProcessed(LocalDate.now());

        expired.forEach(batch -> {
            try {
                expireBatch(batch);
            } catch (Exception e) {
                log.error("Erro ao processar lote vencido {}: {}", batch.getId(), e.getMessage());
            }
        });
    }

    private void expireBatch(ProductBatch batch) {
        if (batch.getRemainingQuantity() <= 0) {
            batch.setStatus(BatchStatus.EXPIRED);
            productBatchRepository.save(batch);
            return;
        }

        Stock stock = batch.getStock();
        Integer previousQuantity = stock.getQuantity();

        // desconta do estoque a quantidade restante do lote vencido
        int quantityToRemove = Math.min(
                batch.getRemainingQuantity(),
                stock.getAvailableQuantity()
        );

        if (quantityToRemove > 0) {
            stock.setQuantity(stock.getQuantity() - quantityToRemove);
            stockRepository.save(stock);

            // registra movimento de vencimento
            stockMovementService.registerStockMovement(
                    stock,
                    null, // sistema — sem usuário
                    MovementType.VENCIMENTO,
                    quantityToRemove,
                    previousQuantity,
                    "Baixa automática por vencimento — lote: " + batch.getBatchCode()
            );
        }

        batch.setRemainingQuantity(0);
        batch.setStatus(BatchStatus.EXPIRED);
        productBatchRepository.save(batch);

        log.info("Lote vencido processado — lote: {}, produto: {}, quantidade baixada: {}",
                batch.getBatchCode(), batch.getProduct().getName(), quantityToRemove);
    }

    private void updateBatchStatus(ProductBatch batch) {
        if (batch.isExpired()) {
            batch.setStatus(BatchStatus.EXPIRED);
        } else if (batch.isExpiring(ALERT_7_DAYS)) {
            batch.setStatus(BatchStatus.EXPIRING);
        } else if (batch.isExpiring(ALERT_15_DAYS)) {
            batch.setStatus(BatchStatus.EXPIRING);
        } else if (batch.isExpiring(ALERT_30_DAYS)) {
            batch.setStatus(BatchStatus.EXPIRING);
        } else {
            batch.setStatus(BatchStatus.ACTIVE);
        }
    }

    private ProductBatch getBatchOrThrow(Long id, Long companyId) {
        return productBatchRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new NotFoundException("Lote não encontrado"));
    }

    private Company getCurrentUserCompany() {
        User currentUser = authenticatedUserService.getCurrentUser();
        if (currentUser.getCompany() == null) {
            throw new BusinessException("Usuário não possui empresa vinculada");
        }
        return currentUser.getCompany();
    }
}
