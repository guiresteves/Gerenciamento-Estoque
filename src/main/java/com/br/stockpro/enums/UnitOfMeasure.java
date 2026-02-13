package com.br.stockpro.enums;

public enum UnitOfMeasure {

    UNIDADE("Unit"),
    KG("Kilogram"),
    G("Gram"),
    L("Liter"),
    ML("Milliliter"),
    BX("Box"),
    PKG("Package");

    private final String description;

    UnitOfMeasure(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
