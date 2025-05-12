// Banco.java
package com.tico.boletogen.entities;

public enum Banco {
    BANCO_DO_BRASIL("001", "Banco do Brasil"),
    ITAU("341", "Banco Itaú"),
    BRADESCO("237", "Banco Bradesco");

    private final String codigo;
    private final String nome;

    Banco(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return codigo + " - " + nome;
    }
}