package com.br.stockpro.service;

import com.br.stockpro.dtos.category.CategoryCreateRequest;
import com.br.stockpro.dtos.category.CategoryResponse;
import com.br.stockpro.dtos.category.CategoryUpdateRequest;
import com.br.stockpro.exceptions.BusinessException;
import com.br.stockpro.exceptions.NotFoundException;
import com.br.stockpro.mapper.CategoryMapper;
import com.br.stockpro.model.Category;
import com.br.stockpro.model.Company;
import com.br.stockpro.model.User;
import com.br.stockpro.repository.CategoryRepository;
import com.br.stockpro.repository.UserRepository;
import com.br.stockpro.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final AuthenticatedUserService authenticatedUserService;

    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        Company company = getCurrentUserCompany();

        if (categoryRepository.existsByNameIgnoreCaseAndCompanyId(request.name(), company.getId())) {
            throw new BusinessException("Já existe uma Categoria com esse nome");
        }

        Category category = categoryMapper.toEntity(request);
        category.setCompany(company);

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAllCategories(Boolean active) {
        Company company = getCurrentUserCompany();

        List<Category> categories = (active != null)
                ? categoryRepository.findAllByCompanyIdAndActive(company.getId(), active)
                : categoryRepository.findAllByCompanyId(company.getId());

        return categories.stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findCategoryById(Long id) {
        Company company = getCurrentUserCompany();
        Category category = getCategoryOrThrow(id, company.getId());
        return categoryMapper.toResponse(category);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest request) {
        Company company = getCurrentUserCompany();
        Category category = getCategoryOrThrow(id, company.getId());

        if (!Boolean.TRUE.equals(category.getActive())) {
            throw new BusinessException("Não é possível alterar uma categoria inativa");
        }

        if (request.name() != null
                && categoryRepository.existsByNameIgnoreCaseAndCompanyIdAndIdNot(
                        request.name(), company.getId(), category.getId())
        ) {
            throw new BusinessException("Já existe uma Categoria com esse nome");
        }

        categoryMapper.updateEntityFromDTO(request, category);
        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(updatedCategory);
    }

    @Transactional
    public CategoryResponse activate(Long id) {
        Company company = getCurrentUserCompany();
        Category category = getCategoryOrThrow(id, company.getId());

        if (Boolean.TRUE.equals(category.getActive())) {
            throw new BusinessException("Categoria já está ativa");
        }

        category.setActive(true);
        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(updatedCategory);
    }

    @Transactional
    public CategoryResponse deactivate(Long id) {
        Company company = getCurrentUserCompany();
        Category category = getCategoryOrThrow(id, company.getId());

        if (!Boolean.TRUE.equals(category.getActive())) {
            throw new BusinessException("Categoria já está inativa");
        }

        category.setActive(false);
        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(updatedCategory);
    }

    private Company getCurrentUserCompany() {
        User currentUser = authenticatedUserService.getCurrentUser();
        if (currentUser.getCompany() == null) {
            throw new BusinessException("Usuário não possui empresa vinculada");
        }
        return currentUser.getCompany();
    }

    private Category getCategoryOrThrow(Long id, Long companyId) {
        return categoryRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));
    }
}
