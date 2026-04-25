package com.br.stockpro.controller;

import com.br.stockpro.dtos.stockAlert.StockAlertResponse;
import com.br.stockpro.dtos.stockAlert.StockAlertSummaryResponse;
import com.br.stockpro.enums.AlertStatus;
import com.br.stockpro.enums.AlertType;
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
    public ResponseEntity<Page<StockAlertResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(stockAlertService.findAll(pageable));
    }

    @GetMapping("/summary")
    public ResponseEntity<StockAlertSummaryResponse> getSummary() {
        return ResponseEntity.ok(stockAlertService.getSummary());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<StockAlertResponse>> findByStatus(
            @PathVariable AlertStatus alertStatus,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(stockAlertService.findByStatus(alertStatus, pageable));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<StockAlertResponse>> findByType(
            @PathVariable AlertType type,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(stockAlertService.findByType(type, pageable));
    }

    @PatchMapping("/{id}/acknowledge")
    public ResponseEntity<StockAlertResponse> acknowledge(@PathVariable Long id) {
        return ResponseEntity.ok(stockAlertService.acknowledge(id));
    }
}
