package com.br.stockpro.controller;

import com.br.stockpro.dtos.company.CompanyCreateRequest;
import com.br.stockpro.dtos.company.CompanyResponse;
import com.br.stockpro.dtos.company.CompanyUpdateRequest;
import com.br.stockpro.exceptions.ErrorResponse;
import com.br.stockpro.security.anotations.CanViewStock;
import com.br.stockpro.security.anotations.IsAdmin;
import com.br.stockpro.service.CompanyService;
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

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(name = "Empresa", description = "Gerenciamento da empresa vinculada ao usuário")
public class CompanyController {

    private final CompanyService companyService;

    @Operation(
            summary = "Criar empresa",
            description = """
                    Cria e vincula uma empresa ao usuário autenticado.
                    Cada usuário só pode ter uma empresa vinculada.
                    Após criar a empresa, configure categorias, produtos e fornecedores.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Empresa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Usuário já possui empresa ou CNPJ já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão de acesso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @IsAdmin
    public ResponseEntity<CompanyResponse> createCompany(
            @RequestBody @Valid CompanyCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                companyService.createCompany(request)
        );
    }

    @Operation(
            summary = "Buscar minha empresa",
            description = "Retorna os dados da empresa vinculada ao usuário autenticado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empresa encontrada"),
            @ApiResponse(responseCode = "404", description = "Usuário não possui empresa vinculada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/me")
    @CanViewStock
    public ResponseEntity<CompanyResponse> getMyCompany() {
        return ResponseEntity.ok(companyService.getCompany());
    }

    @Operation(
            summary = "Atualizar empresa",
            description = """
                    Atualiza os dados da empresa vinculada ao usuário autenticado.
                    O CNPJ não pode ser alterado após o cadastro.
                    Não é possível alterar empresas inativas.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Empresa inativa ou dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão de acesso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/me")
    @IsAdmin
    public ResponseEntity<CompanyResponse> updateMyCompany(
            @RequestBody @Valid CompanyUpdateRequest request
    ) {
        return ResponseEntity.ok(companyService.updateCompany(request));
    }
}
