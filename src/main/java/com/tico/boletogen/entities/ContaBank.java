// ContaBank.java
package com.tico.boletogen.entities;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ContaBank {
    private Banco banco;
    private String agencia;
    private String numero;
    private String carteira;
    private TipoConta tipo;

}