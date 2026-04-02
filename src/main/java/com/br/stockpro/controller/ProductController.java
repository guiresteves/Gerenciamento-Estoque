package com.br.stockpro.controller;

import com.br.stockpro.dtos.product.ProductCreateRequest;
import com.br.stockpro.dtos.product.ProductResponse;
import com.br.stockpro.dtos.product.ProductUpdateRequest;
import com.br.stockpro.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductCreateRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductUpdateRequest request
    ){
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAllProducts() {
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ProductResponse> activateProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ProductResponse> deactivateProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.deactivate(id));
    }
}
