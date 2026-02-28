package com.br.stockpro.controller;

import com.br.stockpro.dtos.company.CompanyResponse;
import com.br.stockpro.dtos.company.CompanyUpdateRequest;
import com.br.stockpro.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<CompanyResponse> getCompany() {
        return ResponseEntity.ok(companyService.getCompany());
    }

    @PutMapping
    public ResponseEntity<CompanyResponse> update(
            @RequestBody @Valid CompanyUpdateRequest request) {

        return ResponseEntity.ok(companyService.update(request));
    }
}
