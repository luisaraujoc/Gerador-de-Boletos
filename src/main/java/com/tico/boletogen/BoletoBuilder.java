package com.tico.boletogen;

public class BoletoBuilder {
    private Banco banco;
    private String cedente;
    private String sacado;
    private String vencimento;
    private String valor;
    private String nossoNumero;

    public BoletoBuilder setBanco(Banco banco) {
        this.banco = banco;
        return this;
    }

    public BoletoBuilder setCedente(String cedente) {
        this.cedente = cedente;
        return this;
    }

    public BoletoBuilder setSacado(String sacado) {
        this.sacado = sacado;
        return this;
    }

    public BoletoBuilder setVencimento(String vencimento) {
        this.vencimento = vencimento;
        return this;
    }

    public BoletoBuilder setValor(String valor) {
        this.valor = valor;
        return this;
    }

    public BoletoBuilder setNossoNumero(String nossoNumero) {
        this.nossoNumero = nossoNumero;
        return this;
    }

    public Boleto build() {
        return new Boleto(banco, cedente, sacado, vencimento, valor, nossoNumero);
    }
}