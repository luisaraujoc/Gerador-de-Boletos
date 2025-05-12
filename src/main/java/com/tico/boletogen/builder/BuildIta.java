// BuildIta.java
package com.tico.boletogen.builder;

import com.tico.boletogen.entities.Banco;
import com.tico.boletogen.entities.Boleto;
import com.tico.boletogen.entities.ContaBank;
import com.tico.boletogen.entities.Pessoa;
import com.tico.boletogen.entities.TituloPag;
import java.time.LocalDate;

public class BuildIta implements BuilderBoleto {
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
        boleto.getConta().setBanco(Banco.ITAU);
    }

    @Override
    public String gerarCampoLivre(String agencia, String conta, String nossoNumero, String carteira) {
        // Tratamento para conta Itaú (formato XXXX-X ou XXXXX-X)
        String contaNumerica = conta.split("-")[0].replaceAll("^0+", "");
        String nossoNumeroNumerico = nossoNumero.replaceAll("[.-]", "").replaceAll("^0+", "");
        
        return String.format("%04d%05d%08d%04d",
                Integer.parseInt(agencia),
                Integer.parseInt(contaNumerica.substring(0, Math.min(5, contaNumerica.length()))),
                Long.parseLong(nossoNumeroNumerico.substring(0, Math.min(8, nossoNumeroNumerico.length()))),
                Integer.parseInt(carteira));
    }

    @Override
    public Boleto getBoleto() {
        return boleto;
    }
}