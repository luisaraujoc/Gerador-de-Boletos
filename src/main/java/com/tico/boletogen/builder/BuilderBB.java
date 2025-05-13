// BuilderBB.java
package com.tico.boletogen.builder;

import com.tico.boletogen.entities.Banco;
import com.tico.boletogen.entities.Boleto;
import com.tico.boletogen.entities.ContaBank;
import com.tico.boletogen.entities.Pessoa;
import com.tico.boletogen.entities.TituloPag;
import java.time.LocalDate;

public class BuilderBB implements BuilderBoleto {
    private final Boleto boleto = new Boleto();

    @Override
    public void buildBeneficiario(Pessoa beneficiario) {
        boleto.setBeneficiario(beneficiario);
    }

    @Override
    public void buildPagador(Pessoa pagador) {
        boleto.setPagador(pagador);
    }

    @Override
    public void buildTitulo(String numeroDocumento, LocalDate dataVencimento, double valor) {
        boleto.setTitulo(new TituloPag(numeroDocumento, dataVencimento, valor));
    }

    @Override
    public void buildDadosBancarios(ContaBank contaBank) {
        boleto.setConta(contaBank);
    }

    @Override
    public void buildBanco() {
        boleto.getConta().setBanco(Banco.BANCO_DO_BRASIL);
    }

    @Override
    public String gerarCampoLivre(String agencia, String conta, String nossoNumero, String carteira) {
        String contaNumerica = conta.split("-")[0].replaceAll("^0+", "");
        String digitoConta = conta.contains("-") ? conta.split("-")[1] : "0";

        return String.format("%04d%08d%011d%02d",
                Integer.parseInt(agencia),
                Integer.parseInt(contaNumerica),
                Long.parseLong(nossoNumero),
                Integer.parseInt(carteira));
    }

    @Override
    public Boleto getBoleto() {
        return boleto;
    }
}