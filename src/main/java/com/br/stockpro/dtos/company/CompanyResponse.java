package com.br.stockpro.dtos.company;

import com.br.stockpro.enums.CompanyType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Dados da empresa retornado pela API")
public record CompanyResponse(

        @Schema(description = "ID da empresa", example = "1")
        Long id,

        @Schema(description = "Razão social da empresa (nome jurídico registrado", example = "Empresa Exemplo LTDA")
        String legalName,

        @Schema(description = "Nome fantasia da empresqa (nome comercial", example = "Exemplo Tech")
        String tradeName,

        @Schema(description = "CNPJ da empresa (cadastro nascional de pessoal jurídica)", example = "12.345.678/0001-90")
        String cnpj,

        @Schema(description = "Inscrição estadual da empresda (IE), utilizada para controle estual fiscal", example = "123456789")
        String stateRegistration,

        @Schema(description = "Inscrição municipal da empresa, para controle municipal", example = "987654321")
        String municipalRegistration,

        @Schema(description = "Tipo de empresa (Ex. LTDA, ME...)", example = "LTDA",
                allowableValues = {"LTDA", "MEI", "EI", "SLU", "SS", "SA"})
        CompanyType companyType,

        @Schema(description = "Telefone principal da empresa", example = "(79) 99999-9999")
        String phone,

        @Schema(description = "Email principal da empresa", example = "contato@empresa.com")
        String email,

        @Schema(description = "Status da Empresa", example = "true")
        Boolean active,

        @Schema(description = "Data de crição no formato ISO 8601", example = "2024-01-15T10:3000Z")
        Instant createdAt,

        @Schema(description = "Data da última atualização no formato ISO 8601", example = "2024-01-15T10:3000Z")
        Instant updatedAt
) {
}
