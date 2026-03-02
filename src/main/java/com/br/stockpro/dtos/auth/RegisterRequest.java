package com.br.stockpro.dtos.auth;

public record RegisterRequest(

        String name,
        String email,
        String password
) {
}
