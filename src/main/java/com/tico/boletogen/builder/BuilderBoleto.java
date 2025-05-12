// BuilderBoleto.java
package com.tico.boletogen.builder;

import com.tico.boletogen.entities.Boleto;
import com.tico.boletogen.entities.ContaBank;
import com.tico.boletogen.entities.Pessoa;
import java.time.LocalDate;

public interface BuilderBoleto {
    void buildBeneficiario(Pessoa beneficiario);
    void buildPagador(Pessoa pagador);
    void buildTitulo(String numeroDocumento, LocalDate dataVencimento, double valor);
    void buildDadosBancarios(ContaBank contaBank);
    void buildBanco();
    String gerarCampoLivre(String agencia, String conta, String nossoNumero, String carteira);
    Boleto getBoleto();
}