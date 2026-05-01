package com.br.stockpro.controller;

import com.br.stockpro.dtos.stockAlert.StockAlertResponse;
import com.br.stockpro.dtos.stockAlert.StockAlertSummaryResponse;
import com.br.stockpro.enums.AlertStatus;
import com.br.stockpro.enums.AlertType;
import com.br.stockpro.security.anotations.CanViewStock;
import com.br.stockpro.security.anotations.IsAdminOrManager;
import com.br.stockpro.service.StockAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-alerts")
@RequiredArgsConstructor
public class StockAlertController {

    private final StockAlertService stockAlertService;

    @GetMapping
    @IsAdminOrManager
    public ResponseEntity<Page<StockAlertResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(stockAlertService.findAll(pageable));
    }

    @GetMapping("/summary")
    @CanViewStock
    public ResponseEntity<StockAlertSummaryResponse> getSummary() {
        return ResponseEntity.ok(stockAlertService.getSummary());
    }

    @GetMapping("/status/{status}")
    @IsAdminOrManager
    public ResponseEntity<Page<StockAlertResponse>> findByStatus(
            @PathVariable AlertStatus alertStatus,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(stockAlertService.findByStatus(alertStatus, pageable));
    }

    @GetMapping("/type/{type}")
    @IsAdminOrManager
    public ResponseEntity<Page<StockAlertResponse>> findByType(
            @PathVariable AlertType type,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(stockAlertService.findByType(type, pageable));
    }

    @PatchMapping("/{id}/acknowledge")
    @IsAdminOrManager
    public ResponseEntity<StockAlertResponse> acknowledge(@PathVariable Long id) {
        return ResponseEntity.ok(stockAlertService.acknowledge(id));
    }
}
