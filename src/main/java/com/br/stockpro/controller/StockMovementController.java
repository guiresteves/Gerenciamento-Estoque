package com.br.stockpro.controller;

import com.br.stockpro.dtos.stockMovement.StockMovementManualRequest;
import com.br.stockpro.dtos.stockMovement.StockMovementResponse;
import com.br.stockpro.enums.MovementType;
import com.br.stockpro.security.anotations.*;
import com.br.stockpro.service.StockMovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/stock-movement")
@RequiredArgsConstructor
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @PostMapping("/manual")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<StockMovementResponse> registerManual(@RequestBody @Valid StockMovementManualRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stockMovementService.registerManualMovement(request));
    }

    @GetMapping
    @IsAdminOrManager
    public ResponseEntity<Page<StockMovementResponse>> findAll(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(stockMovementService.findByCompany(pageable));
    }

    @GetMapping("/product/{productId}")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<Page<StockMovementResponse>> findByProduct(
            @PathVariable Long productId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(stockMovementService.findByProduct(productId, pageable));
    }

    @GetMapping("/stock/{stockId}")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<Page<StockMovementResponse>> findByStock(
            @PathVariable Long stockId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(stockMovementService.findByStock(stockId, pageable));
    }

    @GetMapping("/type/{type}")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<Page<StockMovementResponse>> findByType(
            @PathVariable MovementType type,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(stockMovementService.findByType(type, pageable));
    }

    @GetMapping("/period")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<Page<StockMovementResponse>> findByPeriod(
            @RequestParam Instant start,
            @RequestParam Instant end,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(stockMovementService.findByPeriod(start, end, pageable));
    }

    @GetMapping("/reference")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<List<StockMovementResponse>> findByReference(
            @RequestParam Long referenceId,
            @RequestParam String referenceType
    ) {
        return ResponseEntity.ok(stockMovementService.findByReference(referenceId, referenceType));
    }
}
