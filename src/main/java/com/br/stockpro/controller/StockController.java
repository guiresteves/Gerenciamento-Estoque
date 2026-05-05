package com.br.stockpro.controller;

import com.br.stockpro.dtos.stock.StockCreateRequest;
import com.br.stockpro.dtos.stock.StockResponse;
import com.br.stockpro.dtos.stock.StockUpdateRequest;
import com.br.stockpro.exceptions.ErrorResponse;
import com.br.stockpro.security.anotations.*;
import com.br.stockpro.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@Tag(name = "Estoque", description = "Gerenciamento de estoque por produto")
public class StockController {

    private final StockService stockService;

    @Operation(
            summary = "Criar estoque",
            description = "Cria o registro de estoque para um produto"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Estoque criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Produto já possui estoque ou dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @IsAdminOrManagerOrStocker
    public ResponseEntity<StockResponse> createStock(@RequestBody @Valid StockCreateRequest request) {
        StockResponse response = stockService.createStock(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Buscar estoque por ID",
            description = "Busca um estoque específico pelo ID do stock"
    )
    @GetMapping("/{stockId}")
    @CanViewStock
    public ResponseEntity<StockResponse> findById(@PathVariable Long stockId) {
        return ResponseEntity.ok(stockService.findById(stockId));
    }

    @Operation(
            summary = "Buscar estoque por produto",
            description = "Busca um estoque específico pelo ID do produto"
    )
    @GetMapping("/product/{productId}")
    @CanViewStock
    public ResponseEntity<StockResponse> findByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.findByProductId(productId));
    }

    @Operation(
            summary = "Listar estoque",
            description = "Lista todos os estoques ativos da empresa"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    @CanViewStock
    public ResponseEntity<List<StockResponse>> findAllStock() {
        return ResponseEntity.ok(stockService.findAll());
    }

    @Operation(
            summary = "Listar estoque baixo",
            description = "Retorna produtos com quantidade abaixo do mínimo definido"
    )
    @GetMapping("/low-stock")
    @CanViewStock
    public ResponseEntity<List<StockResponse>> findLowStock() {
        return ResponseEntity.ok(stockService.findLowStock());
    }

    @Operation(
            summary = "Listar produtos zerados",
            description = "Retorna produtos com quantidade igual a zero"
    )
    @GetMapping("/out-of-stock")
    @CanViewStock
    public ResponseEntity<List<StockResponse>> findOutOfStock() {
        return ResponseEntity.ok(stockService.findOutOfStock());
    }

    @Operation(
            summary = "Listar estoque disponível",
            description = "Retorna produtos com quantidade disponível maior que zero"
    )
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

    @Operation(
            summary = "Adicionar estoque",
            description = "Adiciona quantidade ao estoque do produto"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estoque adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Quantidade inválida")
    })
    @PatchMapping("/product/{productId}/add")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<StockResponse> addStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ) {
        return ResponseEntity.ok(stockService.addStock(productId, quantity));
    }

    @Operation(
            summary = "Remover estoque",
            description = "Remove quantidade do estoque. Para produtos com controle de validade, aplica FEFO automaticamente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estoque removido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Estoque insuficiente ou lote vencido")
    })
    @PatchMapping("/product/{productId}/remove")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<StockResponse> removeStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ) {
        return ResponseEntity.ok(stockService.removeStock(productId, quantity));
    }

    @Operation(
            summary = "Ajustar estoque",
            description = "Define uma nova quantidade absoluta para o estoque. Gera movimento de AJUSTE_POSITIVO ou AJUSTE_NEGATIVO automaticamente"
    )
    @PatchMapping("/product/{productId}/adjust")
    @IsAdminOrManager
    public ResponseEntity<StockResponse> adjustStock(
            @PathVariable Long productId,
            @RequestParam Integer newQuantity
    ) {
        return ResponseEntity.ok(stockService.adjustStock(productId, newQuantity));
    }

    @Operation(
            summary = "Reservar estoque",
            description = "Reserva quantidade do estoque disponível"
    )
    @PatchMapping("/product/{productId}/reserve")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<StockResponse> reserveStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ) {
        return ResponseEntity.ok(stockService.reserveStock(productId, quantity));
    }

    @Operation(
            summary = "Liberar reserva",
            description = "Libera quantidade previamente reservada"
    )
    @PatchMapping("/product/{productId}/release-reservation")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<StockResponse> releaseReservation(
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ) {
        return ResponseEntity.ok(stockService.releaseReservation(productId, quantity));
    }
}
