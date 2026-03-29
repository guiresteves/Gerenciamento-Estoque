package com.br.stockpro.service;

import com.br.stockpro.dtos.supplier.SupplierCreateRequest;
import com.br.stockpro.dtos.supplier.SupplierResponse;
import com.br.stockpro.dtos.supplier.SupplierUpdateRequest;
import com.br.stockpro.exceptions.BusinessException;
import com.br.stockpro.exceptions.NotFoundException;
import com.br.stockpro.mapper.SupplierMapper;
import com.br.stockpro.model.Company;
import com.br.stockpro.model.Supplier;
import com.br.stockpro.model.User;
import com.br.stockpro.repository.SupplierRepository;
import com.br.stockpro.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final AuthenticatedUserService authenticatedUserService;

    @Transactional
    public SupplierResponse createSupplier(SupplierCreateRequest request) {

        User currentUser = authenticatedUserService.getCurrentUser();
        Company company = getCurrentUserCompany(currentUser);

        if (supplierRepository.existsByCnpjAndCompanyId(request.cnpj(), company.getId())) {
            throw new BusinessException("Já existe um Fornecedor com esse CNPJ para esta empresa");
        }

        Supplier supplier = supplierMapper.toEntity(request);
        supplier.setCompany(company);

        supplier = supplierRepository.save(supplier);
        return supplierMapper.toResponse(supplier);
    }

    @Transactional(readOnly = true)
    public List<SupplierResponse> findAllSuppliers() {

        User currentUser = authenticatedUserService.getCurrentUser();
        Company company = getCurrentUserCompany(currentUser);

        return supplierRepository.findAllByCompanyId(company.getId())
                .stream()
                .map(supplierMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SupplierResponse findSupplierById(Long id) {

        User currentUser = authenticatedUserService.getCurrentUser();
        Company company = getCurrentUserCompany(currentUser);

        Supplier supplier = supplierRepository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(()-> new NotFoundException("Fornecedor não encontrado."));

        return supplierMapper.toResponse(supplier);
    }

    @Transactional
    public SupplierResponse updateSupplier(Long id, SupplierUpdateRequest request) {

        User currentUser = authenticatedUserService.getCurrentUser();
        Company company = getCurrentUserCompany(currentUser);

        Supplier supplier = supplierRepository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(()-> new NotFoundException("Fornecedor não encontrado."));

        if (!Boolean.TRUE.equals(supplier.getActive())) {
            throw new BusinessException("Não é possível alterar um fornecedor inativo");
        }

        supplierMapper.updateEntityFromDTO(request, supplier);
        Supplier updatedSupplier = supplierRepository.save(supplier);

        return supplierMapper.toResponse(updatedSupplier);
    }

    @Transactional
    public SupplierResponse activate(Long id) {

        User currentUser = authenticatedUserService.getCurrentUser();
        Company company = getCurrentUserCompany(currentUser);

        Supplier supplier = supplierRepository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(()-> new NotFoundException("Fornecedor não encontrado."));

        supplier.setActive(Boolean.TRUE);

        Supplier updatedSupplier  = supplierRepository.save(supplier);
        return supplierMapper.toResponse(updatedSupplier);

    }

    @Transactional
    public SupplierResponse deactivate(Long id) {

        User currentUser = authenticatedUserService.getCurrentUser();
        Company company = getCurrentUserCompany(currentUser);

        Supplier supplier = supplierRepository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(()-> new NotFoundException("Fornercedor não encontrado."));

        supplier.setActive(Boolean.FALSE);

        Supplier updatedSupplier  = supplierRepository.save(supplier);
        return supplierMapper.toResponse(updatedSupplier);

    }

    private Company getCurrentUserCompany(User currentUser) {
        if (currentUser.getCompany() == null) {
            throw new BusinessException("Usuário não possui empresa vinculada");
        }
        return currentUser.getCompany();
    }
}
