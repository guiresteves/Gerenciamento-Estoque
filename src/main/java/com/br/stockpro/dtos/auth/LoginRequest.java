package com.br.stockpro.dtos.auth;

public record LoginRequest(

        String email,
        String password
) {
}
