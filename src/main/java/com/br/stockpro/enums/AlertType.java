package com.br.stockpro.enums;

public enum AlertType {
    LOW_STOCK("Alerta abaixo do estoque"),
    OUT_OF_STOCK("Alerta para estoque zerado"),
    LONG_OUT_OF_STOCK("Alerta para estoque zerado há mais de X dias");

    private final String description;

    AlertType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
