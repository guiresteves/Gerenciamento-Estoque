package com.br.stockpro.controller;

import com.br.stockpro.dtos.productBatch.BatchAlertResponse;
import com.br.stockpro.dtos.productBatch.ProductBatchCreateRequest;
import com.br.stockpro.dtos.productBatch.ProductBatchResponse;
import com.br.stockpro.enums.BatchStatus;
import com.br.stockpro.security.anotations.*;
import com.br.stockpro.service.ProductBatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Gestão de Validade", description = "Controle de lotes e validade de produtos")
public class ProductBatchController {

    private final ProductBatchService productBatchService;

    @Operation(
            summary = "Registrar lote",
            description = """
            Registra um novo lote de produto e dá entrada no estoque automaticamente.
            Só disponível para produtos com `trackExpiration = true`.
            Aplica o princípio FEFO (First Expired First Out) nas saídas.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lote registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Produto sem controle de validade ou lote duplicado"),
            @ApiResponse(responseCode = "404", description = "Produto ou estoque não encontrado")
    })
    @PostMapping
    @IsAdminOrManagerOrStocker
    public ResponseEntity<ProductBatchResponse> createBatch(
            @RequestBody @Valid ProductBatchCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productBatchService.createBatch(request));
    }

    @Operation(
            summary = "Listar lotes",
            description = "Lista todos os lotes ordenados por data de vencimento"
    )
    @GetMapping
    @CanViewStock
    public ResponseEntity<Page<ProductBatchResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(productBatchService.findAll(pageable));
    }

    @Operation(summary = "Buscar lote por ID")
    @GetMapping("/{id}")
    @CanViewStock
    public ResponseEntity<ProductBatchResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productBatchService.findById(id));
    }

    @Operation(
            summary = "Lotes por produto",
            description = "Lista todos os lotes de um produto específico"
    )
    @GetMapping("/product/{productId}")
    @CanViewStock
    public ResponseEntity<Page<ProductBatchResponse>> findByProduct(
            @PathVariable Long productId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(productBatchService.findByProduct(productId, pageable));
    }

    @Operation(
            summary = "Lotes por status",
            description = "Filtra lotes por status: ACTIVE, EXPIRING ou EXPIRED"
    )
    @GetMapping("/status/{status}")
    @CanViewStock
    public ResponseEntity<Page<ProductBatchResponse>> findByStatus(
            @PathVariable BatchStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(productBatchService.findByStatus(status, pageable));
    }

    @Operation(
            summary = "Lotes próximos ao vencimento",
            description = """
            Lista lotes que vencem nos próximos 30 dias, ordenados por urgência.
            - 🔴 Até 7 dias — crítico
            - 🟡 Até 15 dias — atenção
            - 🟢 Até 30 dias — aviso
            """
    )
    @GetMapping("/expiring")
    @CanViewStock
    public ResponseEntity<List<BatchAlertResponse>> findExpiring() {
        return ResponseEntity.ok(productBatchService.findExpiring());
    }
}
