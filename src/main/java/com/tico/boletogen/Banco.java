package com.tico.boletogen;

public class Banco {
    private String nome;
    private String codigo;

    public Banco(String nome, String codigo) {
        this.nome = nome;
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }
}
