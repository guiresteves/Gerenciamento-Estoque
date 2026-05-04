package com.br.stockpro.controller;

import com.br.stockpro.dtos.product.ProductCreateRequest;
import com.br.stockpro.dtos.product.ProductResponse;
import com.br.stockpro.dtos.product.ProductUpdateRequest;
import com.br.stockpro.exceptions.ErrorResponse;
import com.br.stockpro.security.anotations.CanViewStock;
import com.br.stockpro.security.anotations.IsAdminOrManager;
import com.br.stockpro.service.ProductService;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Produto", description = "Gerenciamento de produtos da empresa")
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Criar produto",
            description = """
                    Cria um novo produto vinculado à empresa do usuário autenticado.
                    O código de barras deve ser único por empresa.
                    Para produtos com controle de validade, ative `trackExpiration = true`
                    e registre lotes em /api/batches.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Código de barras já cadastrado, categoria inativa ou dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão de acesso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @IsAdminOrManager
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductCreateRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Atualizar produto",
            description = """
                    Atualiza os dados de um produto.
                    Não é possível alterar produtos inativos.
                    O código de barras deve ser único por empresa.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Produto inativo, barcode duplicado ou dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão de acesso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Produto ou categoria não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}")
    @IsAdminOrManager
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductUpdateRequest request
    ){
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @Operation(
            summary = "Listar produtos",
            description = "Lista todos os produtos da empresa. Use o parâmetro `active` para filtrar por status"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    @CanViewStock
    public ResponseEntity<List<ProductResponse>> findAllProducts(
            @RequestParam(required = false) Boolean active
    ) {
        return ResponseEntity.ok(productService.findAllProducts(active));
    }

    @Operation(
            summary = "Buscar produto por código de barras",
            description = """
                    Busca um produto pelo código de barras (EAN-13 ou similar).
                    Ideal para integração com leitores de barcode no caixa e no estoque.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/barcode/{barcode}")
    @CanViewStock
    public ResponseEntity<ProductResponse> findByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(productService.findByBarcode(barcode));
    }

    @Operation(summary = "Buscar produto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    @CanViewStock
    public ResponseEntity<ProductResponse> findProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @Operation(summary = "Ativar produto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto ativado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Produto já está ativo",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/activate")
    @IsAdminOrManager
    public ResponseEntity<ProductResponse> activateProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.activate(id));
    }

    @Operation(summary = "Desativar produto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto desativado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Produto já está inativo",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/deactivate")
    @IsAdminOrManager
    public ResponseEntity<ProductResponse> deactivateProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.deactivate(id));
    }
}
