package com.br.stockpro.controller;

import com.br.stockpro.dtos.supplier.SupplierCreateRequest;
import com.br.stockpro.dtos.supplier.SupplierResponse;
import com.br.stockpro.dtos.supplier.SupplierUpdateRequest;
import com.br.stockpro.repository.SupplierRepository;
import com.br.stockpro.security.anotations.CanViewStock;
import com.br.stockpro.security.anotations.IsAdmin;
import com.br.stockpro.security.anotations.IsAdminOrManager;
import com.br.stockpro.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping

    public ResponseEntity<SupplierResponse> create(@RequestBody @Valid SupplierCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                supplierService.createSupplier(request)
        );
    }

    @GetMapping
    @IsAdminOrManager
    public ResponseEntity<List<SupplierResponse>> findAllSuppliers(
            @RequestParam(required = false) Boolean active ) {
        return ResponseEntity.ok(supplierService.findAllSuppliers(active));
    }

    @GetMapping("/{id}")
    @IsAdminOrManager
    public ResponseEntity<SupplierResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.findSupplierById(id));
    }

    @PatchMapping("/{id}")
    @IsAdminOrManager
    public ResponseEntity<SupplierResponse> updateSupplier(
            @PathVariable Long id,
            @RequestBody @Valid SupplierUpdateRequest request) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, request));
    }

    @PatchMapping("/{id}/activate")
    @IsAdmin
    public ResponseEntity<SupplierResponse> activateSupplier (@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    @IsAdmin
    public ResponseEntity<SupplierResponse> deactivateSupplier (@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.deactivate(id));
    }
}
