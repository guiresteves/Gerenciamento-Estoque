package com.br.stockpro.controller;

import com.br.stockpro.dtos.stockAlert.StockAlertResponse;
import com.br.stockpro.dtos.stockAlert.StockAlertSummaryResponse;
import com.br.stockpro.enums.AlertStatus;
import com.br.stockpro.enums.AlertType;
import com.br.stockpro.exceptions.ErrorResponse;
import com.br.stockpro.security.anotations.CanViewStock;
import com.br.stockpro.security.anotations.IsAdminOrManager;
import com.br.stockpro.service.StockAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-alerts")
@RequiredArgsConstructor
@Tag(name = "Alertas de Estoque", description = "Monitoramento e gestão de alertas de estoque")
public class StockAlertController {

    private final StockAlertService stockAlertService;

    @Operation(
            summary = "Listar alertas",
            description = "Lista todos os alertas de estoque da empresa paginados, ordenados do mais recente para o mais antigo"
    )
    @GetMapping
    @IsAdminOrManager
    public ResponseEntity<Page<StockAlertResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(stockAlertService.findAll(pageable));
    }

    @Operation(
            summary = "Resumo dos alertas",
            description = """
                    Retorna a contagem de alertas ativos agrupados por tipo.
                    Ideal para alimentar o dashboard do sistema.
                    
                    Tipos de alerta:
                    - `LOW_STOCK` → quantidade abaixo do mínimo definido
                    - `OUT_OF_STOCK` → produto zerado
                    - `LONG_OUT_OF_STOCK` → zerado há mais de 7 dias
                    - `ABOVE_MAXIMUM` → quantidade acima do máximo definido
                    """
    )
    @ApiResponse(responseCode = "200", description = "Resumo retornado com sucesso")
    @GetMapping("/summary")
    @CanViewStock
    public ResponseEntity<StockAlertSummaryResponse> getSummary() {
        return ResponseEntity.ok(stockAlertService.getSummary());
    }

    @Operation(
            summary = "Filtrar alertas por status",
            description = """
                    Filtra alertas pelo status:
                    - `ACTIVE` → alerta ativo, ainda não resolvido
                    - `RESOLVED` → estoque normalizado automaticamente
                    - `ACKNOWLEDGED` → operador reconheceu o alerta
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Status inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/status/{status}")
    @IsAdminOrManager
    public ResponseEntity<Page<StockAlertResponse>> findByStatus(
            @PathVariable AlertStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(stockAlertService.findByStatus(status, pageable));
    }

    @Operation(
            summary = "Filtrar alertas por tipo",
            description = """
                    Filtra alertas pelo tipo:
                    - `LOW_STOCK` → abaixo do mínimo
                    - `OUT_OF_STOCK` → produto zerado
                    - `LONG_OUT_OF_STOCK` → zerado há mais de 7 dias
                    - `ABOVE_MAXIMUM` → acima do máximo
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Tipo inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/type/{type}")
    @IsAdminOrManager
    public ResponseEntity<Page<StockAlertResponse>> findByType(
            @PathVariable AlertType type,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(stockAlertService.findByType(type, pageable));
    }

    @Operation(
            summary = "Reconhecer alerta",
            description = """
                    Marca um alerta como reconhecido pelo operador.
                    Indica que o responsável está ciente do problema.
                    Apenas alertas com status `ACTIVE` podem ser reconhecidos.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alerta reconhecido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Alerta não está ativo",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão de acesso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Alerta não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/acknowledge")
    @IsAdminOrManager
    public ResponseEntity<StockAlertResponse> acknowledge(@PathVariable Long id) {
        return ResponseEntity.ok(stockAlertService.acknowledge(id));
    }
}
