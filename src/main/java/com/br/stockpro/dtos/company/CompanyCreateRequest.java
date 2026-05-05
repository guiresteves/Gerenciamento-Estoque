package com.br.stockpro.dtos.company;

import com.br.stockpro.enums.CompanyType;
import com.br.stockpro.validation.annotation.ValidCNPJ;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados necessários para um criação da empresa")
public record CompanyCreateRequest(

        @Schema(description = "Razão Social da empresa (nome jurídico registrado", example = "Empresa Exemplo LTDA",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(min = 3, max = 250, message = "Razão Social deve ter entre 3 a 250 caracteres")
        @NotBlank(message = "Razão social é obrigatório")
        String legalName,

        @Schema(description = "Nome fantazia da empresa (nome comercial)", example = "Exemplo Tech",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(min = 3, max = 250, message = "Nome fantasia deve ter entre 3 a 250 caracteres")
        @NotBlank(message = "Nome Fantasia é obrigatório")
        String tradeName,

        @Schema(description = "CNPJ da empresa (Cadastro Nascional de Pessoa Jurídica", example = "12.345.678/0001-90",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "CNPJ é obrigatório")
        @ValidCNPJ
        String cnpj,

        @Schema(description = "Inscrição estadual da empresa (IE), utilizada para controle fiscal estadual", example = "123456789",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 50, message = "Inscrição estadual deve ter no máximo 50 caracteres")
        String stateRegistration,

        @Schema(description = "Inscrição municipal da empresa, utilizada no controle fiscal municipal", example = "987654321",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 50, message = "Inscrição municipal deve ter no máximo 50 caracteres")
        String municipalRegistration,

        @Schema(description = "Tipo de empresa (ex. LTDA, ME)", example = "LTDA",
                allowableValues = {"LTDA", "MEI", "EI", "SLU", "SS", "SA"},
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O tipo de empresa é obrigatório")
        CompanyType companyType,

        @Schema(description = "Telefone principal da empresa", example = "(79) 99999-9999",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Telefone é obrigatório")
        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        @Pattern(regexp = "[0-9()\\-\\s+]+", message = "Telefone Inválido")
        String phone,

        @Schema(description = "Email principal da empresa", example = "contato@empresa.com",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @Email(message = "Email Inválido")
        @NotBlank(message = "Email é obrigatório")
        @Size(max = 150, message = "O email deve ter no máximo 150 caracteres")
        String email
) {
}
