package com.br.stockpro.enums;

public enum UnitOfMeasure {

    UNIDADE("Unidade"),
    KG("Quilograma"),
    G("Grama"),
    L("Litro"),
    ML("Mililitro"),
    CX("Caixa"),
    PCT("Pacote");

    private final String descricao;

    UnitOfMeasure(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
