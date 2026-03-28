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
    public CategoryResponse creatCategory(CategoryCreateRequest request) {

        User currentUser = authenticatedUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new BusinessException("Usuário não possui empresa vinculada");
        }

        Company company = currentUser.getCompany();

        if (categoryRepository.existsByNameIgnoreCaseAndCompanyId(request.name(), company.getId())) {
            throw new BusinessException("Já existe uma Categoria com esse nome");
        }

        Category category = categoryMapper.toEntity(request);
        category.setCompany(company);

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAllCategories() {

        User currentUser = authenticatedUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new BusinessException("Usuário não possui empresa vinculada");
        }

        Company company = currentUser.getCompany();

        return categoryRepository.findAllByByCompanyId(company.getId())
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findCategoryById(Long id) {

        User currentUser = authenticatedUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new BusinessException("Usuário não possui empresa vinculada");
        }

        Company company = currentUser.getCompany();

        Category category = categoryRepository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada."));

        return categoryMapper.toResponse(category);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest request) {

        User currentUser = authenticatedUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new BusinessException("Usuário não possui empresa vinculada");
        }

        Company company = currentUser.getCompany();

        Category category = categoryRepository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        if (!Boolean.TRUE.equals(category.getActive())) {
            throw new BusinessException("Não é possível alterar uma categoria inativa");
        }

        if (request.name() != null
                && categoryRepository.existsByNameIgnoreCaseAndCompanyIdAndIdNot(
                        request.name(),
                        company.getId(),
                        category.getId())
        ) {
            throw new BusinessException("Já existe uma Categoria com esse nome");
        }

        categoryMapper.updateEntityFromDTO(request, category);
        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(updatedCategory);
    }

    @Transactional
    public CategoryResponse activate(Long id) {
        User currentUser = authenticatedUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new BusinessException("Usuário não possui empresa vinculada");
        }

        Company company = currentUser.getCompany();

        Category category = categoryRepository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        category.setActive(true);

        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(updatedCategory);
    }

    @Transactional
    public CategoryResponse deactivate(Long id) {
        User currentUser = authenticatedUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new BusinessException("Usuário não possui empresa vinculada");
        }

        Company company = currentUser.getCompany();

        Category category = categoryRepository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        category.setActive(false);

        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(updatedCategory);
    }
}
