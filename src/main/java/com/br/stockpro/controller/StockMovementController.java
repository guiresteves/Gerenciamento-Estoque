package com.br.stockpro.controller;

import com.br.stockpro.dtos.stockMovement.StockMovementManualRequest;
import com.br.stockpro.dtos.stockMovement.StockMovementResponse;
import com.br.stockpro.enums.MovementType;
import com.br.stockpro.exceptions.ErrorResponse;
import com.br.stockpro.security.anotations.*;
import com.br.stockpro.service.StockMovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/stock-movement")
@RequiredArgsConstructor
@Tag(name = "Movimentação de Estoque", description = "Registro e consulta de movimentações de estoque")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @Operation(
            summary = "Registrar movimento manual",
            description = """
                    Registra uma movimentação manual de estoque.
                    
                    Tipos permitidos para movimentação manual:
                    - `ENTRADA` → entrada de mercadoria
                    - `AJUSTE_POSITIVO` → correção de inventário para mais
                    - `AJUSTE_NEGATIVO` → correção de inventário para menos
                    - `DEVOLUCAO` → devolução de cliente
                    - `VENCIMENTO` → baixa por produto vencido
                    
                    Tipos NÃO permitidos manualmente (use endpoints específicos):
                    - `SAIDA` → use /api/stocks/product/{id}/remove
                    - `RESERVA` → use /api/stocks/product/{id}/reserve
                    - `LIBERACAO` → use /api/stocks/product/{id}/release-reservation
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movimento registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Tipo de movimento não permitido ou estoque insuficiente",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão de acesso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Estoque não encontrado para o produto",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/manual")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<StockMovementResponse> registerManual(@RequestBody @Valid StockMovementManualRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stockMovementService.registerManualMovement(request));
    }

    @Operation(
            summary = "Listar movimentos da empresa",
            description = "Lista todas as movimentações da empresa paginadas, ordenadas da mais recente para a mais antiga"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    @IsAdminOrManager
    public ResponseEntity<Page<StockMovementResponse>> findAll(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(stockMovementService.findByCompany(pageable));
    }

    @Operation(
            summary = "Listar movimentos por produto",
            description = "Lista todas as movimentações de um produto específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/product/{productId}")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<Page<StockMovementResponse>> findByProduct(
            @PathVariable Long productId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(stockMovementService.findByProduct(productId, pageable));
    }

    @Operation(
            summary = "Listar movimentos por estoque",
            description = "Lista todas as movimentações de um estoque específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Estoque não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/stock/{stockId}")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<Page<StockMovementResponse>> findByStock(
            @PathVariable Long stockId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(stockMovementService.findByStock(stockId, pageable));
    }

    @Operation(
            summary = "Listar movimentos por tipo",
            description = """
                    Filtra movimentos pelo tipo:
                    `ENTRADA`, `SAIDA`, `AJUSTE_POSITIVO`, `AJUSTE_NEGATIVO`,
                    `RESERVA`, `LIBERACAO`, `VENCIMENTO`, `DEVOLUCAO`
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Tipo inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/type/{type}")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<Page<StockMovementResponse>> findByType(
            @PathVariable MovementType type,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(stockMovementService.findByType(type, pageable));
    }

    @Operation(
            summary = "Listar movimentos por período",
            description = """
                    Filtra movimentos por período de data.
                    Os parâmetros `start` e `end` devem estar no formato ISO 8601.
                    Exemplo: `2024-01-01T00:00:00Z`
                    A data inicial não pode ser maior que a data final.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Período inválido — start maior que end",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/period")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<Page<StockMovementResponse>> findByPeriod(
            @RequestParam Instant start,
            @RequestParam Instant end,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(stockMovementService.findByPeriod(start, end, pageable));
    }

    @Operation(
            summary = "Listar movimentos por referência",
            description = """
                    Busca movimentos vinculados a uma referência externa.
                    Útil para rastrear movimentos gerados por pedidos, vendas ou outros módulos.
                    Exemplo: `referenceId=1&referenceType=ORDER`
                    """
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/reference")
    @IsAdminOrManagerOrStocker
    public ResponseEntity<List<StockMovementResponse>> findByReference(
            @RequestParam Long referenceId,
            @RequestParam String referenceType
    ) {
        return ResponseEntity.ok(stockMovementService.findByReference(referenceId, referenceType));
    }
}
