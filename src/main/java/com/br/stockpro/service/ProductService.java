package com.br.stockpro.service;

import com.br.stockpro.dtos.product.ProductCreateRequest;
import com.br.stockpro.dtos.product.ProductResponse;
import com.br.stockpro.dtos.product.ProductUpdatedRequest;
import com.br.stockpro.exceptions.BusinessException;
import com.br.stockpro.mapper.ProductMapper;
import com.br.stockpro.model.Category;
import com.br.stockpro.model.Company;
import com.br.stockpro.model.Product;
import com.br.stockpro.repository.CategoryRepository;
import com.br.stockpro.repository.CompanyRepository;
import com.br.stockpro.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponse create(ProductCreateRequest request) {

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new BusinessException("Empresa não encontrada"));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new BusinessException("Categoria não encontrada"));

        if (!category.getCompany().getId().equals(company.getId())) {
            throw new BusinessException("Categoria não pertence à empresa informada");
        }

        boolean barcodeExists = productRepository
                .existsByCompanyIdAndBarcode(company.getId(), request.barcode());

        if (barcodeExists) {
            throw new BusinessException("Já existe produto com esse código de barras nesta empresa");
        }

        Product product = productMapper.toEntity(request);

        product.setCompany(company);
        product.setCategory(category);

        Product saved = productRepository.save(product);

        return productMapper.toResponseDTO(saved);
    }

    @Transactional
    public ProductResponse update(Long id, ProductUpdatedRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado"));

        productMapper.updateEntityFromDTO(request, product);

        if (request.categoryId() != null) {

            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new BusinessException("Categoria não encontrada"));

            if (!category.getCompany().getId().equals(product.getCompany().getId())) {
                throw new BusinessException("Categoria não pertence à empresa do produto");
            }

            product.setCategory(category);
        }

        Product updated = productRepository.save(product);

        return productMapper.toResponseDTO(updated);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado"));

        return productMapper.toResponseDTO(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {

        return productRepository.findAllByActiveTrue()
                .stream()
                .map(productMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public void delete(Long productId, Long companyId) {

        Product product = productRepository
                .findByIdAndCompanyIdAndActiveTrue(productId, companyId)
                .orElseThrow(() -> new BusinessException("Produto não encontrado"));

        product.setActive(false);

        productRepository.save(product);
    }
}
