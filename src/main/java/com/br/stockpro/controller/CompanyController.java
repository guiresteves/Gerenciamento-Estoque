package com.br.stockpro.controller;

import com.br.stockpro.dtos.company.CompanyResponse;
import com.br.stockpro.dtos.company.CompanyUpdateRequest;
import com.br.stockpro.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/me")
    public ResponseEntity<CompanyResponse> getMyCompany() {
        return ResponseEntity.ok(companyService.getMyCompany());
    }

    @PutMapping("/me")
    public ResponseEntity<CompanyResponse> updateMyCompany(
            @RequestBody @Valid CompanyUpdateRequest request
    ) {
        return ResponseEntity.ok(companyService.updateMyCompany(request));
    }
}
