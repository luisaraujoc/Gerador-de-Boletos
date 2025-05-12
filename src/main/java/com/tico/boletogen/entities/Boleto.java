// Boleto.java
package com.tico.boletogen.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Boleto {
    private Pessoa beneficiario = new Pessoa();
    private Pessoa pagador = new Pessoa();
    private ContaBank conta = new ContaBank();
    private TituloPag titulo = new TituloPag("", LocalDate.now(), 0.0);
    private String numero = (int)(Math.random()*99999)+ 1 + "";
    private LocalDate dataEmissao = LocalDate.now();
    private String localPagamento = "Pagável em qualquer banco até o vencimento";
    private String instrucoes = "Não receber após o vencimento";
    private String codigoBarras;
    private String linhaDigitavel;
    private String carteira;
}