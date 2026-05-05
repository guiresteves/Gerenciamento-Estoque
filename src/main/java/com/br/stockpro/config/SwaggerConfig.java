package com.br.stockpro.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(buildInfo())
                .servers(buildServers())
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", buildSecurityScheme()));
    }

    private Info buildInfo() {
        return new Info()
                .title("StockPro API")
                .description("""
                        ERP de gestão de estoque para supermercados.
                        
                        ## Autenticação
                        A API utiliza JWT Bearer Token. Para autenticar:
                        1. Faça o registro em `/api/auth/register`
                        2. Faça o login em `/api/auth/login`
                        3. Copie o token retornado
                        4. Clique em **Authorize** e cole o token
                        
                        ## Multi-tenant
                        Cada empresa tem acesso apenas aos seus próprios dados.
                        Após o registro, crie sua empresa em `/api/companies`.
                        """)
                .version("1.0.0")
                .contact(new Contact()
                        .name("StockPro")
                        .email("guiresteves@gmai.com"))
                .license(new License()
                        .name("MIT License"));
    }

    private List<Server> buildServers() {
        return List.of(
                new Server()
                        .url("http://localhost:8080")
                        .description("Ambiente local"),
                new Server()
                        .url("https://api.stockpro.com.br")
                        .description("Ambiente de produção")
        );
    }

    private SecurityScheme buildSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Insira o token JWT obtido no login");
    }
}