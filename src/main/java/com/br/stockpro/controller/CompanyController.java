package com.br.stockpro.controller;

import com.br.stockpro.dtos.company.CompanyCreateRequest;
import com.br.stockpro.dtos.company.CompanyResponse;
import com.br.stockpro.dtos.company.CompanyUpdateRequest;
import com.br.stockpro.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(
            @RequestBody @Valid CompanyCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                companyService.createCompany(request)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<CompanyResponse> getMyCompany() {
        return ResponseEntity.ok(companyService.getCompany());
    }

    @PatchMapping("/me")
    public ResponseEntity<CompanyResponse> updateMyCompany(
            @RequestBody @Valid CompanyUpdateRequest request
    ) {
        return ResponseEntity.ok(companyService.updateCompany(request));
    }
}
