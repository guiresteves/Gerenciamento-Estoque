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

        User currentUser = authenticatedUserService.getCurrentUser();
        Company company = getCurrentUserCompany(currentUser);

        if (productRepository.existsByBarcodeAndCompanyId(request.barcode(), company.getId())){
            throw new BusinessException("Já existe um produto com esse código de barra");
        }

        Category category = categoryRepository.findByIdAndCompanyId(request.categoryId(), company.getId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        Product product = productMapper.toEntity(request);
        product.setCategory(category);
        product.setCompany(company);

        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAllProducts(){

        User currentUser = authenticatedUserService.getCurrentUser();
        Company company = getCurrentUserCompany(currentUser);

        return productRepository.findAllByCompanyId(company.getId())
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findProductById(Long id){

        User currentUser = authenticatedUserService.getCurrentUser();
        Company company = getCurrentUserCompany(currentUser);

        Product product = productRepository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        return productMapper.toResponse(product);
    }

    public ProductResponse updateProduct(Long id, ProductUpdateRequest request){

        User currentUser = authenticatedUserService.getCurrentUser();
        Company company = getCurrentUserCompany(currentUser);

        Product product = productRepository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        if (!Boolean.TRUE.equals(product.getActive())){
            throw new BusinessException("Não é possivel alterar produto inativo");
        }

        if (request.barcode() != null &&
                productRepository.existsByBarcodeAndCompanyIdAndIdNot(
                        request.barcode(),
                        company.getId(),
                        product.getId()
        )) {
            throw new BusinessException("Já existe um produto com esse código de barras para esta empresa");
        }

        if (request.categoryId() != null){

            Category category = categoryRepository.findByIdAndCompanyId(request.categoryId(), company.getId())
                    .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

            product.setCategory(category);
        }

        productMapper.updateEntityFromDTO(request, product);

        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Transactional
    public ProductResponse activate(Long id){

        User currentUser = authenticatedUserService.getCurrentUser();
        Company company = getCurrentUserCompany(currentUser);

        Product product = productRepository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        product.setActive(true);

        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Transactional
    public ProductResponse deactivate(Long id){

        User currentUser = authenticatedUserService.getCurrentUser();
        Company company = getCurrentUserCompany(currentUser);

        Product product = productRepository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        product.setActive(false);

        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    private Company getCurrentUserCompany(User currentUser) {
        if (currentUser.getCompany() == null) {
            throw new BusinessException("Usuário não possui empresa vinculada");
        }
        return currentUser.getCompany();
    }
}
