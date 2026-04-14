package com.br.stockpro.service;

import com.br.stockpro.dtos.stock.StockCreateRequest;
import com.br.stockpro.dtos.stock.StockResponse;
import com.br.stockpro.dtos.stock.StockUpdateRequest;
import com.br.stockpro.exceptions.BusinessException;
import com.br.stockpro.exceptions.NotFoundException;
import com.br.stockpro.mapper.StockMapper;
import com.br.stockpro.model.Company;
import com.br.stockpro.model.Product;
import com.br.stockpro.model.Stock;
import com.br.stockpro.model.User;
import com.br.stockpro.repository.ProductRepository;
import com.br.stockpro.repository.StockRepository;
import com.br.stockpro.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockMapper stockMapper;
    private final StockRepository stockRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final ProductRepository productRepository;


    @Transactional
    public StockResponse createStock(StockCreateRequest request) {

        Company company = getAuthenticatedCompany();

        Product product = productRepository.findByIdAndCompanyId(request.productId(), company.getId())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        if (stockRepository.existsByProductIdAndCompanyId(product.getId(), company.getId())) {
            throw new BusinessException("Já existe estoque cadastrado para este produto");
        }

        Stock stock = stockMapper.toEntity(request);
        stock.setCompany(company);
        stock.setProduct(product);

        validateStockData(stock);
        validateReservedQuantity(stock);

        Stock savedStock = stockRepository.save(stock);
        return stockMapper.toResponse(savedStock);
    }

    @Transactional(readOnly = true)
    public StockResponse findById(Long id) {

        Company company = getAuthenticatedCompany();

        Stock stock = stockRepository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new NotFoundException("Estoque não encontrado"));

        return stockMapper.toResponse(stock);
    }

    @Transactional(readOnly = true)
    public StockResponse findByProductId(Long productId) {

        Company company = getAuthenticatedCompany();

        Stock stock = stockRepository.findByProductIdAndCompanyId(productId, company.getId())
                .orElseThrow(() -> new NotFoundException("Estoque não econtrado"));

        return stockMapper.toResponse(stock);
    }

    @Transactional(readOnly = true)
    public List<StockResponse> findAll() {

        Company company = getAuthenticatedCompany();

        return stockRepository.findAllByCompanyIdAndActiveTrue(company.getId())
                .stream()
                .map(stockMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StockResponse> findLowStock() {

        Company company = getAuthenticatedCompany();

        return stockRepository.findLowStockByCompanyId(company.getId())
                .stream()
                .map(stockMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StockResponse> findOutOfStock() {

        Company company = getAuthenticatedCompany();

        return stockRepository.findOutOfStockByCompanyId(company.getId())
                .stream()
                .map(stockMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StockResponse> findAvailableStock() {

        Company company = getAuthenticatedCompany();

        return stockRepository.findAvailableStockByCompanyId(company.getId())
                .stream()
                .map(stockMapper::toResponse)
                .toList();
    }

    @Transactional
    public StockResponse updateStock(Long stockId, StockUpdateRequest request) {

        Company company = getAuthenticatedCompany();

        Stock stock = stockRepository.findByIdAndCompanyId(stockId, company.getId())
                .orElseThrow(() -> new NotFoundException("Estoque não encontrado"));

        stockMapper.updateEntityFromDTO(request, stock);

        validateStockData(stock);
        validateReservedQuantity(stock);

        Stock updated = stockRepository.save(stock);
        return stockMapper.toResponse(updated);
    }

    @Transactional
    public StockResponse addStock(Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BusinessException("A quantidade de entrada deve ser maior que zero");
        }

        Stock stock = getStockByProductId(productId);

        stock.setQuantity(stock.getQuantity() + quantity);
        validateStockData(stock);
        validateReservedQuantity(stock);

        Stock updated = stockRepository.save(stock);
        return stockMapper.toResponse(updated);
    }

    @Transactional
    public StockResponse removeStock(Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BusinessException("A quantidade de saída deve ser maior que zero");
        }

        Stock stock = getStockByProductId(productId);

        if (stock.getAvailableQuantity() < quantity) {
            throw new BusinessException("Estoque disponível insuficiente para a saída");
        }

        stock.setQuantity(stock.getQuantity() - quantity);
        validateStockData(stock);
        validateReservedQuantity(stock);

        Stock updated = stockRepository.save(stock);
        return stockMapper.toResponse(updated);
    }

    @Transactional
    public StockResponse adjustStock(Long productId, Integer newQuantity) {
        if (newQuantity == null || newQuantity < 0) {
            throw new BusinessException("A nova quantidade não pode ser negativa");
        }

        Stock stock = getStockByProductId(productId);

        if (newQuantity < stock.getReservedQuantity()) {
            throw new BusinessException("A nova quantidade não pode ser menor que a quantidade reservada");
        }

        stock.setQuantity(newQuantity);
        validateStockData(stock);
        validateReservedQuantity(stock);

        Stock updated = stockRepository.save(stock);
        return stockMapper.toResponse(updated);
    }

    @Transactional
    public StockResponse reserveStock(Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BusinessException("A quantidade para reserva deve ser maior que zero");
        }

        Stock stock = getStockByProductId(productId);

        if (stock.getAvailableQuantity() < quantity) {
            throw new BusinessException("Estoque disponível insuficiente para reserva");
        }

        stock.setReservedQuantity(stock.getReservedQuantity() + quantity);
        validateReservedQuantity(stock);

        Stock updated = stockRepository.save(stock);
        return stockMapper.toResponse(updated);
    }

    @Transactional
    public StockResponse releaseReservation(Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BusinessException("A quantidade para liberação deve ser maior que zero");
        }

        Stock stock = getStockByProductId(productId);

        if (stock.getReservedQuantity() < quantity) {
            throw new BusinessException("A quantidade a liberar é maior que a quantidade reservada");
        }

        stock.setReservedQuantity(stock.getReservedQuantity() - quantity);
        validateReservedQuantity(stock);

        Stock updated = stockRepository.save(stock);
        return stockMapper.toResponse(updated);
    }


    private Company getAuthenticatedCompany() {
        User currentUser = authenticatedUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new BusinessException("Usuário não possui empresa vinculada");
        }

        return currentUser.getCompany();
    }

    private Stock getStockByProductId(Long productId) {
        Company company = getAuthenticatedCompany();

        return stockRepository.findByProductIdAndCompanyIdAndActiveTrue(productId, company.getId())
                .orElseThrow(() -> new NotFoundException("Estoque não encontrado para o produto informado"));
    }

    private void validateStockData(Stock stock) {
        if (stock.getQuantity() == null || stock.getQuantity() < 0) {
            throw new BusinessException("A quantidade em estoque não pode ser negativa");
        }

        if (stock.getReservedQuantity() == null || stock.getReservedQuantity() < 0) {
            throw new BusinessException("A quantidade reservada não pode ser negativa");
        }

        if (stock.getMinQuantity() == null || stock.getMinQuantity() < 0) {
            throw new BusinessException("A quantidade mínima não pode ser negativa");
        }
    }

    private void validateReservedQuantity(Stock stock) {
        if (stock.getReservedQuantity() > stock.getQuantity()) {
            throw new BusinessException("A quantidade reservada não pode ser maior que a quantidade total em estoque");
        }
    }
}
