package com.br.stockpro.controller;

import com.br.stockpro.dtos.alertStock.AlertStockResponse;
import com.br.stockpro.dtos.alertStock.StockAlertSummaryResponse;
import com.br.stockpro.enums.AlertStatus;
import com.br.stockpro.enums.AlertType;
import com.br.stockpro.service.AlertStockService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-alerts")
@RequiredArgsConstructor
public class AlertSrtockController {

    private final AlertStockService alertStockService;

    @GetMapping
    public ResponseEntity<Page<AlertStockResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(alertStockService.findAll(pageable));
    }

    @GetMapping("/summary")
    public ResponseEntity<StockAlertSummaryResponse> getSummary() {
        return ResponseEntity.ok(alertStockService.getSummary());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<AlertStockResponse>> findByStatus(
            @PathVariable AlertStatus alertStatus,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(alertStockService.findByStatus(alertStatus, pageable));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<AlertStockResponse>> findByType(
            @PathVariable AlertType type,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(alertStockService.findByType(type, pageable));
    }

    @PatchMapping("/{id}/acknowledge")
    public ResponseEntity<AlertStockResponse> acknowledge(@PathVariable Long id) {
        return ResponseEntity.ok(alertStockService.acknowledge(id));
    }
}
