package com.br.stockpro.dtos.supplier;

import com.br.stockpro.validation.annotation.ValidCNPJ;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados para criação de um fornecedor")
public record SupplierCreateRequest(

        @Schema(description = "Razão social do fornecedor — nome jurídico registrado",
                example = "Distribuidora Bebidas LTDA",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(min = 3, max = 250, message = "Razão Social deve ter entre 3 a 250 caracteres")
        @NotBlank(message = "Razão social é obrigatório")
        String legalName,

        @Schema(description = "Nome fantasia do fornecedor — nome comercial",
                example = "Distribuidora Bebidas",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(min = 3, max = 250, message = "Nome fantasia deve ter entre 3 a 250 caracteres")
        @NotBlank(message = "Nome Fantasia é obrigatório")
        String tradeName,

        @Schema(description = "CNPJ do fornecedor — Cadastro Nacional de Pessoa Jurídica. Validado matematicamente",
                example = "98765432000110",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "CNPJ é obrigatório")
        @ValidCNPJ
        String cnpj,

        @Schema(description = "Inscrição estadual do fornecedor — utilizada para controle fiscal estadual",
                example = "123456789",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 50, message = "Inscrição estadual deve ter no máximo 50 caracteres")
        String stateRegistration,

        @Schema(description = "Inscrição municipal do fornecedor — utilizada para controle fiscal municipal",
                example = "987654321",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 50, message = "Inscrição municipal deve ter no máximo 50 caracteres") // corrigido
        String municipalRegistration,

        @Schema(description = "Telefone principal do fornecedor",
                example = "(79) 99999-9999",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Telefone é obrigatório")
        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        @Pattern(regexp = "[0-9()\\-\\s+]+", message = "Telefone Inválido")
        String phone,

        @Schema(description = "Email principal do fornecedor",
                example = "contato@distribuidora.com.br",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @Email(message = "Email Inválido")
        @NotBlank(message = "Email é obrigatório")
        @Size(max = 150, message = "O email deve ter no máximo 150 caracteres")
        String email,

        @Schema(description = "Nome do responsável principal do fornecedor",
                example = "José Carlos",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 150, message = "O nome do responsável deve ter no máximo 150 caracteres")
        String contactName
) {}