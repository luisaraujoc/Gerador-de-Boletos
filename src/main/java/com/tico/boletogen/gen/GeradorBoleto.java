// GeradorBoleto.java
package com.tico.boletogen.gen;

import com.tico.boletogen.builder.BuilderBoleto;
import com.tico.boletogen.entities.*;
import java.time.LocalDate;

public class GeradorBoleto {
    private final BuilderBoleto builder;

    public GeradorBoleto(BuilderBoleto builder) {
        this.builder = builder;
    }

    public Boleto gerarBoleto(
            Pessoa beneficiario,
            Pessoa pagador,
            String numeroDocumento,
            LocalDate vencimento,
            double valor,
            String agencia,
            TipoConta tipoConta,
            String carteira,
            String numeroConta,
            String nossoNumero
    ) {
        return gerarBoleto(beneficiario, pagador, numeroDocumento, vencimento, valor,
                agencia, tipoConta, carteira, numeroConta, nossoNumero, null);
    }

    public Boleto gerarBoleto(
            Pessoa beneficiario,
            Pessoa pagador,
            String numeroDocumento,
            LocalDate vencimento,
            double valor,
            String agencia,
            TipoConta tipoConta,
            String carteira,
            String numeroConta,
            String nossoNumero,
            String codigoCedente
    ) {
        builder.buildBeneficiario(beneficiario);
        builder.buildPagador(pagador);
        builder.buildTitulo(numeroDocumento, vencimento, valor);

        ContaBank contaBank = new ContaBank();
        contaBank.setAgencia(agencia);
        contaBank.setNumero(numeroConta);
        contaBank.setTipo(tipoConta);
        contaBank.setCarteira(carteira);

        builder.buildDadosBancarios(contaBank);
        builder.buildBanco();

        Boleto boleto = builder.getBoleto();
        boleto.setNumero(nossoNumero != null ? nossoNumero : numeroDocumento);
        boleto.setCodigoBarras(CodigoBarraUtils.gerarCodigoBarras(boleto, builder));
        boleto.setLinhaDigitavel(formatarLinhaDigitavel(boleto.getCodigoBarras()));

        return boleto;
    }

    private String formatarLinhaDigitavel(String codigoBarras) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < codigoBarras.length(); i += 5) {
            int end = Math.min(i + 5, codigoBarras.length());
            sb.append(codigoBarras.substring(i, end)).append(" ");
        }
        return sb.toString().trim();
    }
}