package com.br.stockpro.dtos.supplier;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "Dados do fornecedor retornados pela API")
public record SupplierResponse(

        @Schema(description = "ID do fornecedor", example = "1")
        Long id,

        @Schema(description = "Razão social do fornecedor — nome jurídico registrado",
                example = "Distribuidora Bebidas LTDA")
        String legalName,

        @Schema(description = "Nome fantasia do fornecedor — nome comercial", // corrigido Fantasma → Fantasia
                example = "Distribuidora Bebidas")
        String tradeName,

        @Schema(description = "CNPJ do fornecedor — Cadastro Nacional de Pessoa Jurídica", // corrigido Nascional → Nacional
                example = "98765432000110")
        String cnpj,

        @Schema(description = "Inscrição estadual do fornecedor", example = "123456789", nullable = true)
        String stateRegistration,

        @Schema(description = "Inscrição municipal do fornecedor", example = "987654321", nullable = true)
        String municipalRegistration,

        @Schema(description = "Telefone principal do fornecedor", example = "(79) 99999-9999")
        String phone,

        @Schema(description = "Email principal do fornecedor", example = "contato@distribuidora.com.br")
        String email,

        @Schema(description = "Nome do responsável principal do fornecedor",
                example = "José Carlos", nullable = true)
        String contactName,

        @Schema(description = "Status do fornecedor", example = "true")
        Boolean active,

        @Schema(description = "Data de criação no formato ISO 8601", example = "2024-01-15T10:30:00Z")
        Instant createdAt,

        @Schema(description = "Data da última atualização no formato ISO 8601", example = "2024-01-15T10:30:00Z")
        Instant updatedAt
) {}