package com.br.stockpro.controller;

import com.br.stockpro.dtos.category.CategoryCreateRequest;
import com.br.stockpro.dtos.category.CategoryResponse;
import com.br.stockpro.dtos.category.CategoryUpdateRequest;
import com.br.stockpro.exceptions.ErrorResponse;
import com.br.stockpro.security.anotations.CanViewStock;
import com.br.stockpro.security.anotations.IsAdminOrManager;
import com.br.stockpro.service.CategoryService;
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
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categoria", description = "Gerenciamento de categorias de produtos")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Criar categoria",
            description = "Cria uma nova categoria vinculada à empresa do usuário autenticado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Categoria já existe ou dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão de acesso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @IsAdminOrManager
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestBody @Valid CategoryCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.createCategory(request));
    }

    @Operation(
            summary = "Listar categorias",
            description = "Lista todas as categorias da empresa. Use o parâmetro `active` para filtrar por status"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    @CanViewStock
    public ResponseEntity<List<CategoryResponse>> findAllCategories(
            @RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(categoryService.findAllCategories(active));
    }

    @Operation(summary = "Buscar categoria por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    @CanViewStock
    public ResponseEntity<CategoryResponse> findCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }

    @Operation(
            summary = "Atualizar categoria",
            description = "Atualiza os dados de uma categoria. Não é possível alterar categorias inativas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Categoria inativa ou nome já existe",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}")
    @IsAdminOrManager
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CategoryUpdateRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @Operation(summary = "Ativar categoria")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria ativada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Categoria já está ativa",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/activate")
    @IsAdminOrManager
    public ResponseEntity<CategoryResponse> activateCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.activate(id));
    }

    @Operation(summary = "Desativar categoria")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria desativada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Categoria já está inativa",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/deactivate")
    @IsAdminOrManager
    public ResponseEntity<CategoryResponse> deactivateCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.deactivate(id));
    }
}
