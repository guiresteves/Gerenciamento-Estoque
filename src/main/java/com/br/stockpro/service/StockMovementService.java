package com.br.stockpro.service;

import com.br.stockpro.dtos.stockMovement.StockMovementManualRequest;
import com.br.stockpro.dtos.stockMovement.StockMovementResponse;
import com.br.stockpro.enums.MovementOrigin;
import com.br.stockpro.enums.MovementType;
import com.br.stockpro.exceptions.BusinessException;
import com.br.stockpro.exceptions.NotFoundException;
import com.br.stockpro.mapper.StockMovementMapper;
import com.br.stockpro.model.Company;
import com.br.stockpro.model.Stock;
import com.br.stockpro.model.StockMovement;
import com.br.stockpro.model.User;
import com.br.stockpro.repository.StockMovementRepository;
import com.br.stockpro.repository.StockRepository;
import com.br.stockpro.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockMovementService {


    private final StockMovementRepository stockMovementRepository;
    private final StockRepository stockRepository;

    private final AuthenticatedUserService authenticatedUserService;

    private final StockMovementMapper stockRepositoryMapper;


    @Transactional
    public void registerStockMovement(
            Stock stock,
            User performedBy,
            MovementType type,
            Integer quantity,
            Integer previousQuantity,
            String reason
    ) {
        StockMovement movement = StockMovement.builder()
                .stock(stock)
                .product(stock.getProduct())
                .company(stock.getCompany())
                .performedBy(performedBy)
                .movementType(type)
                .movementOrigin(performedBy != null
                        ? MovementOrigin.SYSTEM
                        : MovementOrigin.SYSTEM)
                .quantity(quantity)
                .previousQuantity(previousQuantity)
                .currentQuantity(stock.getQuantity())
                .reason(reason)
                .build();

        stockMovementRepository.save(movement);
    }

    @Transactional
    public void registerSystemMovementWithReference(
            Stock stock,
            User performedBy,
            MovementType type,
            Integer quantity,
            Integer previousQuantity,
            String reason,
            Long referenceId,
            String referenceType
    ) {

        StockMovement movement = StockMovement.builder()
                .stock(stock)
                .product(stock.getProduct())
                .company(stock.getCompany())
                .performedBy(performedBy)
                .movementType(type)
                .movementOrigin(MovementOrigin.SYSTEM)
                .quantity(quantity)
                .previousQuantity(previousQuantity)
                .currentQuantity(stock.getQuantity())
                .reason(reason)
                .referenceId(referenceId)
                .referenceType(referenceType)
                .build();

        stockMovementRepository.save(movement);
    }

    @Transactional
    public StockMovementResponse registerManualMovement(StockMovementManualRequest request){

        User currentUser = authenticatedUserService.getCurrentUser();
        Company company = getCurrentUserCompany();

        validateManualMovementType(request.movementType());

        Stock stock = stockRepository.findByProductIdAndCompanyIdAndActiveTrue(request.productId(), company.getId())
                .orElseThrow(() -> new NotFoundException("Estoque não encontrado para o produto informado"));

        Integer previousQuantity = stock.getQuantity();

        applyMovement(stock, request.movementType(), request.quantity());

        stockRepository.save(stock);

        StockMovement movement = StockMovement.builder()
                .stock(stock)
                .product(stock.getProduct())
                .company(company)
                .performedBy(currentUser)
                .movementType(request.movementType())
                .movementOrigin(MovementOrigin.MANUAL)
                .quantity(request.quantity())
                .previousQuantity(previousQuantity)
                .currentQuantity(stock.getQuantity())
                .reason(request.reason())
                .build();

        StockMovement saved = stockMovementRepository.save(movement);
        return stockRepositoryMapper.toResponse(saved);

    }

    @Transactional
    public StockMovement registerStockMovementAndReturn(
            Stock stock,
            User performedBy,
            MovementType type,
            Integer quantity,
            Integer previousQuantity,
            String reason
    ) {
        StockMovement movement = StockMovement.builder()
                .stock(stock)
                .product(stock.getProduct())
                .company(stock.getCompany())
                .performedBy(performedBy)
                .movementType(type)
                .movementOrigin(MovementOrigin.SYSTEM)
                .quantity(quantity)
                .previousQuantity(previousQuantity)
                .currentQuantity(stock.getQuantity())
                .reason(reason)
                .build();

        return stockMovementRepository.save(movement);
    }

    // Consultas

    @Transactional(readOnly = true)
    public Page<StockMovementResponse> findByCompany(Pageable pageable) {
        Company company = getCurrentUserCompany();
        return stockMovementRepository
                .findByCompanyIdOrderByCreatedAtDesc(company.getId(), pageable)
                .map(stockRepositoryMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<StockMovementResponse> findByProduct(Long productId, Pageable pageable) {
        Company company = getCurrentUserCompany();
        return stockMovementRepository
                .findByProductIdAndCompanyIdOrderByCreatedAtDesc(productId, company.getId(), pageable)
                .map(stockRepositoryMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<StockMovementResponse> findByStock(Long stockId, Pageable pageable) {
        Company company = getCurrentUserCompany();
        return stockMovementRepository
                .findByStockIdAndCompanyIdOrderByCreatedAtDesc(stockId, company.getId(), pageable)
                .map(stockRepositoryMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<StockMovementResponse> findByType(MovementType type, Pageable pageable) {
        Company company = getCurrentUserCompany();
        return stockMovementRepository
                .findByCompanyIdAndMovementTypeOrderByCreatedAtDesc(company.getId(), type, pageable)
                .map(stockRepositoryMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<StockMovementResponse> findByPeriod(Instant start, Instant end, Pageable pageable) {
        if (start.isAfter(end)) {
            throw new BusinessException("A data inicial não pode ser maior que a data final");
        }
        Company company = getCurrentUserCompany();
        return stockMovementRepository
                .findByCompanyIdAndPeriod(company.getId(), start, end, pageable)
                .map(stockRepositoryMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<StockMovementResponse> findByReference(Long referenceId, String referenceType) {
        return stockMovementRepository
                .findByReference(referenceId, referenceType)
                .stream()
                .map(stockRepositoryMapper::toResponse)
                .toList();
    }

    private void applyMovement(Stock stock, MovementType type, Integer quantity) {
        switch (type) {
            case AJUSTE_POSITIVO, ENTRADA, DEVOLUCAO -> stock.setQuantity(stock.getQuantity() + quantity);

            case AJUSTE_NEGATIVO, VENCIMENTO -> {
                if (stock.getAvailableQuantity() < quantity) {
                    throw new BusinessException("Quantidade disponível insuficiente para este movimento");
                }
                stock.setQuantity(stock.getQuantity() - quantity);
            }

            default -> throw new BusinessException("Tipo de movimento não permitido para operação manual. Use os endpoints específicos de reserva e liberação");
        }
    }

    private void validateManualMovementType(MovementType type) {
        if (type == MovementType.RESERVA || type == MovementType.LIBERACAO || type == MovementType.SAIDA) {
            throw new BusinessException("Este tipo de movimento não pode ser registrado manualmente");
        }
    }

    private Company getCurrentUserCompany() {
        User currentUser = authenticatedUserService.getCurrentUser();
        if (currentUser.getCompany() == null) {
            throw new BusinessException("Usuário não possui empresa vinculada");
        }
        return currentUser.getCompany();
    }
}
