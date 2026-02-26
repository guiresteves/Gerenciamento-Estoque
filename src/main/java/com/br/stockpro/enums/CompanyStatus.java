package com.br.stockpro.enums;

public enum CompanyStatus {
    ACTIVE("Pode usar o sistema normalmente"),
    INACTIVE("Desativada manualmente"),
    SUSPENDED("Inadimplência"),
    TRIAL("Período de teste"),
    CANCELLED("Cancelado");

    private final String description;

    CompanyStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
