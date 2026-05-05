package com.br.stockpro.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação contendo o token JWT e dados do usuário")
public record AuthResponse(

        @Schema(description = "Token JWT para autenticação", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,

        @Schema(description = "Tipo do token", example = "Bearer")
        String type,

        @Schema(description = "ID do usuário", example = "1")
        Long userId,

        @Schema(description = "Nome do usuário", example = "Admin StockPro")
        String name,

        @Schema(description = "Email do usuário", example = "admin@stockpro.com")
        String email,

        @Schema(description = "Role do usuário no sistema", example = "ADMIN",
                allowableValues = {"ADMIN", "MANAGER", "STOCKER", "CASHIER", "FINANCIAL"})
        String role,

        @Schema(description = "ID da empresa vinculada. Null se o usuário ainda não possui empresa",
                example = "1", nullable = true)
        Long companyId
) {
}
