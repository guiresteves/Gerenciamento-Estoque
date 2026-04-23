package com.br.stockpro.enums;

public enum AlertStatus {
    ACTIVE("Alerta ativo"),
    RESOLVED("Estoque normalizado"),
    ACKNOWLEDGED("Operador visualizou ou reconheceu o alerta");

    private final String description;

    AlertStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
