package com.br.stockpro.controller;

import com.br.stockpro.dtos.productBatch.BatchAlertResponse;
import com.br.stockpro.dtos.productBatch.ProductBatchCreateRequest;
import com.br.stockpro.dtos.productBatch.ProductBatchResponse;
import com.br.stockpro.enums.BatchStatus;
import com.br.stockpro.service.ProductBatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
@RequiredArgsConstructor
public class ProductBatchController {

    private final ProductBatchService productBatchService;

    @PostMapping
    public ResponseEntity<ProductBatchResponse> createBatch(
            @RequestBody @Valid ProductBatchCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productBatchService.createBatch(request));
    }

    @GetMapping
    public ResponseEntity<Page<ProductBatchResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(productBatchService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBatchResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productBatchService.findById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<ProductBatchResponse>> findByProduct(
            @PathVariable Long productId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(productBatchService.findByProduct(productId, pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ProductBatchResponse>> findByStatus(
            @PathVariable BatchStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(productBatchService.findByStatus(status, pageable));
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<BatchAlertResponse>> findExpiring() {
        return ResponseEntity.ok(productBatchService.findExpiring());
    }
}
