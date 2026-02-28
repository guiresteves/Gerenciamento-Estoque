package com.br.stockpro.service;

import com.br.stockpro.dtos.company.CompanyResponse;
import com.br.stockpro.dtos.company.CompanyUpdateRequest;
import com.br.stockpro.exceptions.NotFoundException;
import com.br.stockpro.mapper.CompanyMapper;
import com.br.stockpro.model.Company;
import com.br.stockpro.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Transactional(readOnly = true)
    public CompanyResponse getCompany() {

        Company company = companyRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow( () ->
                        new NotFoundException("Empresa não cadastrada"));

        return companyMapper.toResponse(company);
    }

    public CompanyResponse update(CompanyUpdateRequest request) {

        Company company = companyRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow( () ->
                        new NotFoundException("Empresa não cadastrada"));

        companyMapper.updateEntityFromDTO(request, company);
        return companyMapper.toResponse(company);
    }
}
