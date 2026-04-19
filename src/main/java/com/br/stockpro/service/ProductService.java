package com.br.stockpro.service;

import com.br.stockpro.dtos.product.ProductCreateRequest;
import com.br.stockpro.dtos.product.ProductResponse;
import com.br.stockpro.dtos.product.ProductUpdateRequest;
import com.br.stockpro.exceptions.BusinessException;
import com.br.stockpro.exceptions.NotFoundException;
import com.br.stockpro.mapper.ProductMapper;
import com.br.stockpro.model.Category;
import com.br.stockpro.model.Company;
import com.br.stockpro.model.Product;
import com.br.stockpro.model.User;
import com.br.stockpro.repository.CategoryRepository;
import com.br.stockpro.repository.ProductRepository;
import com.br.stockpro.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final AuthenticatedUserService authenticatedUserService;

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request){
        Company company = getCurrentUserCompany();

        validateStockLimits(request.minStock(), request.maxStock());

        if (productRepository.existsByBarcodeAndCompanyId(request.barcode(), company.getId())){
            throw new BusinessException("Já existe um produto com esse código de barra");
        }

        Category category = getActiveCategoryOrThrow(request.categoryId(), company.getId());

        if (!Boolean.TRUE.equals(category.getActive())) {
            throw new BusinessException("Não é possível vincular produto a uma categoria inativa");
        }

        Product product = productMapper.toEntity(request);
        product.setCompany(company);
        product.setCategory(category);

        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAllProducts(Boolean active){
        Company company = getCurrentUserCompany();

        List<Product> products = (active != null)
                ? productRepository.findAllByCompanyIdAndActive(company.getId(), active)
                : productRepository.findAllByCompanyId(company.getId());

        return products.stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findByBarcode(String barcode) {
        Company company = getCurrentUserCompany();
        return productRepository.findByBarcodeAndCompanyId(barcode, company.getId())
                .map(productMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
    }

    @Transactional(readOnly = true)
    public ProductResponse findProductById(Long id){
        Company company = getCurrentUserCompany();
        Product product = getProductOrThrow(id, company.getId());
        return productMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request){
        Company company = getCurrentUserCompany();
        Product product = getProductOrThrow(id, company.getId());

        if (!Boolean.TRUE.equals(product.getActive())){
            throw new BusinessException("Não é possivel alterar produto inativo");
        }

        validateStockLimits(request.minStock(), request.maxStock());

        if (request.barcode() != null &&
                productRepository.existsByBarcodeAndCompanyIdAndIdNot(
                        request.barcode(), company.getId(), product.getId()
        )) {
            throw new BusinessException("Já existe um produto com esse código de barras para esta empresa");
        }

        if (request.categoryId() != null) {
            Category category = getActiveCategoryOrThrow(request.categoryId(), company.getId());
            product.setCategory(category);
        }

        productMapper.updateEntityFromDTO(request, product);

        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Transactional
    public ProductResponse activate(Long id){
        Company company = getCurrentUserCompany();
        Product product = getProductOrThrow(id, company.getId());

        if (Boolean.TRUE.equals(product.getActive())) {
            throw new BusinessException("Produto já está ativo");
        }

        product.setActive(true);

        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Transactional
    public ProductResponse deactivate(Long id){
        Company company = getCurrentUserCompany();
        Product product = getProductOrThrow(id, company.getId());

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new BusinessException("Produto já está inativo");
        }

        product.setActive(false);

        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    private void validateStockLimits(Integer minStock, Integer maxStock) {
        if (minStock != null && maxStock != null && minStock > maxStock) {
            throw new BusinessException("Estoque mínimo não pode ser maior que o estoque máximo");
        }
    }

    private Category getActiveCategoryOrThrow(Long categoryId, Long companyId) {
        Category category = categoryRepository.findByIdAndCompanyId(categoryId, companyId)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        if (!Boolean.TRUE.equals(category.getActive())) {
            throw new BusinessException("Não é possível vincular produto a uma categoria inativa");
        }

        return category;
    }

    private Product getProductOrThrow(Long id, Long companyId) {
        return productRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
    }

    private Company getCurrentUserCompany() {
        User currentUser = authenticatedUserService.getCurrentUser();
        if (currentUser.getCompany() == null) {
            throw new BusinessException("Usuário não possui empresa vinculada");
        }
        return currentUser.getCompany();
    }
}
