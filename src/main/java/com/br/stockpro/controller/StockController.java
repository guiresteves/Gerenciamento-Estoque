package com.br.stockpro.controller;

import com.br.stockpro.dtos.stock.StockCreateRequest;
import com.br.stockpro.dtos.stock.StockResponse;
import com.br.stockpro.dtos.stock.StockUpdateRequest;
import com.br.stockpro.security.anotations.*;
import com.br.stockpro.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping
    @IsAdminOrManagerOrStocker
    public ResponseEntity<StockResponse> createStock(@RequestBody @Valid StockCreateRequest request) {
        StockResponse response = stockService.createStock(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{stockId}")
    @CanViewStock
    public ResponseEntity<StockResponse> findById(@PathVariable Long stockId) {
        return ResponseEntity.ok(stockService.findById(stockId));
    }

    @GetMapping("/product/{productId}")
    @CanViewStock
    public ResponseEntity<StockResponse> findByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.findByProductId(productId));
    }

    @GetMapping
    @CanViewStock
    public ResponseEntity<List<StockResponse>> findAllStock() {
        return ResponseEntity.ok(stockService.findAll());
    }

    @GetMapping("/low-stock")
    @CanViewStock
    public ResponseEntity<List<StockResponse>> findLowStock() {
        return ResponseEntity.ok(stockService.findLowStock());
    }

    @GetMapping("/out-of-stock")
    @CanViewStock
    public ResponseEntity<List<StockResponse>> findOutOfStock() {
        return ResponseEntity.ok(stockService.findOutOfStock());
    }

    @GetMapping("/available")
    @CanViewStock
    public ResponseEntity<List<StockResponse>> findAvailableStock() {
        return ResponseEntity.ok(stockService.findAvailableStock());
    }

    @PatchMapping("/{stockId}")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<StockResponse> updateStock(
            @PathVariable Long stockId,
            @RequestBody @Valid StockUpdateRequest request
    ) {
        return ResponseEntity.ok(stockService.updateStock(stockId, request));
    }

    @PatchMapping("/product/{productId}/add")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<StockResponse> addStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ) {
        return ResponseEntity.ok(stockService.addStock(productId, quantity));
    }

    @PatchMapping("/product/{productId}/remove")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<StockResponse> removeStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ) {
        return ResponseEntity.ok(stockService.removeStock(productId, quantity));
    }

    @PatchMapping("/product/{productId}/adjust")
    @IsAdminOrManager
    public ResponseEntity<StockResponse> adjustStock(
            @PathVariable Long productId,
            @RequestParam Integer newQuantity
    ) {
        return ResponseEntity.ok(stockService.adjustStock(productId, newQuantity));
    }

    @PatchMapping("/product/{productId}/reserve")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<StockResponse> reserveStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ) {
        return ResponseEntity.ok(stockService.reserveStock(productId, quantity));
    }

    @PatchMapping("/product/{productId}/release-reservation")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<StockResponse> releaseReservation(
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ) {
        return ResponseEntity.ok(stockService.releaseReservation(productId, quantity));
    }
}
