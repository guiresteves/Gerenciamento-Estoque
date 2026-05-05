package com.br.stockpro.dtos.company;

import com.br.stockpro.enums.CompanyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados para a atualização da empresa, todos os campos são opcional")
public record CompanyUpdateRequest(

        @Schema(description = "Nova Razão Social da empresa (nome jurídico registrado", example = "Empresa Tecnologia LTDA")
        @Size(min = 3, max = 250, message = "Razão social deve ter entre 3 e 250 caracteres")
        String legalName,

        @Schema(description = "Novo nome fantazia da empresa (nome comercial)", example = "Tech Tecnology")
        @Size(min = 3, max = 250, message = "Nome fantasia deve ter entre 3 e 250 caracteres")
        String tradeName,

        @Schema(description = "Nova inscrição estadual da empresa (IE), utilizada para controle fiscal estadual", example = "987654321")
        @Size(max = 50, message = "Inscrição estadual deve ter no máximo 50 caracteres")
        String stateRegistration,

        @Schema(description = "Nova inscrição municipal da empresa, utilizada no controle fiscal municipal", example = "123456789")
        @Size(max = 50, message = "Inscrição municipal deve ter no máximo 50 caracteres")
        String municipalRegistration,

        @Schema(description = "Novo tipo de empresa (ex. LTDA, ME)", example = "LTDA",
                allowableValues = {"LTDA", "MEI", "EI", "SLU", "SS", "SA"})
        CompanyType companyType,

        @Schema(description = "Novo telefone principal da empresa", example = "(79) 99999-9999")
        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        @Pattern(regexp = "[0-9()\\-\\s+]+", message = "Telefone inválido")
        String phone,

        @Schema(description = "Novo email principal da empresa", example = "contato@empresa.com")
        @Email(message = "Email inválido")
        @Size(max = 150, message = "Email deve ter no máximo 150 caracteres")
        String email
) {
}
