package com.br.stockpro.dtos.auth;

public record AuthResponse(

        String token,
        String type,
        Long userId,
        String name,
        String email,
        String role,
        Long companyId
) {
}
