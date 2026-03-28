package com.br.stockpro.service;

import com.br.stockpro.dtos.company.CompanyCreateRequest;
import com.br.stockpro.dtos.company.CompanyResponse;
import com.br.stockpro.dtos.company.CompanyUpdateRequest;
import com.br.stockpro.exceptions.BusinessException;
import com.br.stockpro.exceptions.NotFoundException;
import com.br.stockpro.mapper.CompanyMapper;
import com.br.stockpro.model.Company;
import com.br.stockpro.model.User;
import com.br.stockpro.repository.CompanyRepository;
import com.br.stockpro.repository.UserRepository;
import com.br.stockpro.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final AuthenticatedUserService authenticatedUserService;
    private final UserRepository userRepository;

    @Transactional
    public CompanyResponse createMyCompany(CompanyCreateRequest request) {

        User currentUser = authenticatedUserService.getCurrentUser();

        if (currentUser.getCompany() != null) {
            throw new NotFoundException("Usuário já possui empresa vinculada");
        }

        if (companyRepository.existsByCnpj(request.cnpj())) {
            throw new NotFoundException("Usuário não possui empresa vinculada");
        }

        Company company = companyMapper.toEntity(request);
        Company savedCompany = companyRepository.save(company);

        currentUser.setCompany(savedCompany);
        userRepository.save(currentUser);

        return companyMapper.toResponse(savedCompany);
    }

    @Transactional(readOnly = true)
    public CompanyResponse getMyCompany() {

        User currentUser = authenticatedUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new NotFoundException("Usuário não possui empresa vinculada");
        }

        Company company = companyRepository.findById(currentUser.getCompany().getId())
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada"));

        return companyMapper.toResponse(company);
    }

    @Transactional
    public CompanyResponse updateMyCompany(CompanyUpdateRequest request) {

        User currentUser = authenticatedUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new NotFoundException("Usuário não possui empresa vinculada");
        }

        Company company = companyRepository.findById(currentUser.getCompany().getId())
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada"));

        if (!Boolean.TRUE.equals(company.getActive())) {
            throw new BusinessException("Não é possível alterar uma empresa inativa");
        }

        companyMapper.updateEntityFromDTO(request, company);
        Company updatedCompany = companyRepository.save(company);

        return companyMapper.toResponse(updatedCompany);
    }
}
