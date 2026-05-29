package com.br.stockpro.controller;

import com.br.stockpro.dtos.supplier.SupplierCreateRequest;
import com.br.stockpro.dtos.supplier.SupplierResponse;
import com.br.stockpro.dtos.supplier.SupplierUpdateRequest;
import com.br.stockpro.exceptions.ErrorResponse;
import com.br.stockpro.security.anotations.IsAdmin;
import com.br.stockpro.security.anotations.IsAdminOrManager;
import com.br.stockpro.service.SupplierService;
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
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@Tag(name = "Fornecedor", description = "Gerenciamento de fornecedores da empresa")
public class SupplierController {

    private final SupplierService supplierService;

    @Operation(
            summary = "Criar fornecedor",
            description = """
                    Cria um novo fornecedor vinculado à empresa do usuário autenticado.
                    O CNPJ deve ser único por empresa.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Fornecedor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "CNPJ já cadastrado ou dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão de acesso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @IsAdminOrManager
    public ResponseEntity<SupplierResponse> create(@RequestBody @Valid SupplierCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                supplierService.createSupplier(request)
        );
    }

    @Operation(
            summary = "Listar fornecedores",
            description = "Lista todos os fornecedores da empresa. Use o parâmetro `active` para filtrar por status"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    @IsAdminOrManager
    public ResponseEntity<List<SupplierResponse>> findAllSuppliers(
            @RequestParam(required = false) Boolean active ) {
        return ResponseEntity.ok(supplierService.findAllSuppliers(active));
    }

    @Operation(summary = "Buscar fornecedor por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fornecedor encontrado"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    @IsAdminOrManager
    public ResponseEntity<SupplierResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.findSupplierById(id));
    }

    @Operation(
            summary = "Atualizar fornecedor",
            description = "Atualiza os dados de um fornecedor. Não é possível alterar fornecedores inativos"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fornecedor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Fornecedor inativo ou dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão de acesso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}")
    @IsAdminOrManager
    public ResponseEntity<SupplierResponse> updateSupplier(
            @PathVariable Long id,
            @RequestBody @Valid SupplierUpdateRequest request) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, request));
    }

    @Operation(summary = "Ativar fornecedor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fornecedor ativado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Fornecedor já está ativo",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/activate")
    @IsAdmin
    public ResponseEntity<SupplierResponse> activateSupplier (@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.activate(id));
    }

    @Operation(summary = "Desativar fornecedor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fornecedor desativado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Fornecedor já está inativo",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/deactivate")
    @IsAdmin
    public ResponseEntity<SupplierResponse> deactivateSupplier (@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.deactivate(id));
    }
}
