package com.tico.boletogen;

public class Boleto {
    private Banco banco;
    private String cedente;
    private String sacado;
    private String vencimento;
    private String valor;
    private String nossoNumero;

    public Boleto(Banco banco, String cedente, String sacado,
                  String vencimento, String valor, String nossoNumero) {
        this.banco = banco;
        this.cedente = cedente;
        this.sacado = sacado;
        this.vencimento = vencimento;
        this.valor = valor;
        this.nossoNumero = nossoNumero;
    }

    // Getters
    public Banco getBanco() { return banco; }
    public String getCedente() { return cedente; }
    public String getSacado() { return sacado; }
    public String getVencimento() { return vencimento; }
    public String getValor() { return valor; }
    public String getNossoNumero() { return nossoNumero; }
}
