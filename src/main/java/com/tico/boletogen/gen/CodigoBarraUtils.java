// CodigoBarraUtils.java
package com.tico.boletogen.gen;

import com.tico.boletogen.entities.Boleto;
import com.tico.boletogen.builder.BuilderBoleto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class CodigoBarraUtils {
    private static final LocalDate DATA_BASE = LocalDate.of(1997, 10, 7);

    private CodigoBarraUtils() {}

    public static String calcularFatorVencimento(LocalDate vencimento) {
        long dias = ChronoUnit.DAYS.between(DATA_BASE, vencimento);
        return String.format("%04d", dias);
    }

    public static String formatarValor(double valor) {
        return BigDecimal.valueOf(valor)
                .setScale(2, RoundingMode.HALF_EVEN)
                .toString()
                .replace(".", "")
                .replace(",", "")
                .replaceAll("^0+(?!$)", "");
    }

    public static int calcularDigitoVerificadorModulo11(String codigo) {
        int soma = 0;
        int peso = 2;

        for (int i = codigo.length() - 1; i >= 0; i--) {
            int num = Character.getNumericValue(codigo.charAt(i));
            soma += num * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }

        int resto = soma % 11;
        int digito = 11 - resto;

        return (digito == 0 || digito == 10 || digito == 11) ? 1 : digito;
    }

    public static String gerarCodigoBarras(Boleto boleto, BuilderBoleto builder) {
        String banco = boleto.getConta().getBanco().getCodigo();
        String moeda = "9";
        String fatorVencimento = calcularFatorVencimento(boleto.getTitulo().getDataVencimento());
        String valor = formatarValor(boleto.getTitulo().getValor());

        String campoLivre = builder.gerarCampoLivre(
                boleto.getConta().getAgencia(),
                boleto.getConta().getNumero(),
                boleto.getNumero(),
                boleto.getConta().getCarteira() != null ? boleto.getConta().getCarteira() : "0"
        );

        String parcial = banco + moeda + fatorVencimento + valor + campoLivre;
        int digito = calcularDigitoVerificadorModulo11(parcial);

        return banco + moeda + digito + fatorVencimento + valor + campoLivre;
    }
}