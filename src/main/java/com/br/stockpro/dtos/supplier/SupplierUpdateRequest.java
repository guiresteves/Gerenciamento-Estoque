package com.br.stockpro.dtos.supplier;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados para atualização de um fornecedor — todos os campos são opcionais")
public record SupplierUpdateRequest(

        @Schema(description = "Nova razão social do fornecedor", example = "Distribuidora Bebidas e Sucos LTDA")
        @Size(min = 3, max = 250, message = "Razão social deve ter entre 3 e 250 caracteres")
        String legalName,

        @Schema(description = "Novo nome fantasia do fornecedor", example = "Distribuidora Bebidas e Sucos")
        @Size(min = 3, max = 250, message = "Nome fantasia deve ter entre 3 e 250 caracteres")
        String tradeName,

        @Schema(description = "Nova inscrição estadual do fornecedor", example = "123456789")
        @Size(max = 50, message = "Inscrição estadual deve ter no máximo 50 caracteres")
        String stateRegistration,

        @Schema(description = "Nova inscrição municipal do fornecedor", example = "987654321")
        @Size(max = 50, message = "Inscrição municipal deve ter no máximo 50 caracteres")
        String municipalRegistration,

        @Schema(description = "Novo telefone do fornecedor", example = "(79) 98888-8888")
        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        @Pattern(regexp = "[0-9()\\-\\s+]+", message = "Telefone inválido")
        String phone,

        @Schema(description = "Novo email do fornecedor", example = "novo@distribuidora.com.br")
        @Email(message = "Email inválido")
        @Size(max = 150, message = "Email deve ter no máximo 150 caracteres")
        String email,

        @Schema(description = "Novo nome do responsável principal", example = "Maria Oliveira")
        @Size(max = 150, message = "O nome do responsável deve ter no máximo 150 caracteres")
        String contactName
) {}