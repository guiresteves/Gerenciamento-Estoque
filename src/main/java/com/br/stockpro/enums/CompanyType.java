package com.br.stockpro.enums;

public enum CompanyType {

    MEI("Microempreendedor Individual"),
    EI("Empresário Individual"),
    SLU("Sociedade Limitada Unipessoal"),
    LTDA("Sociedade Limitada"),
    SS("Sociedade Simples"),
    SA("Sociedade Anônima");

    private final String description;

    CompanyType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
