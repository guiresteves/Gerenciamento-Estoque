package com.br.stockpro.enums;

public enum UnidadeMedida {

    UNIDADE("Unidade"),
    KG("Quilograma"),
    G("Grama"),
    L("Litro"),
    ML("Mililitro"),
    CX("Caixa"),
    PCT("Pacote");

    private final String descricao;

    UnidadeMedida(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
