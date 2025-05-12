// Pessoa.java
package com.tico.boletogen.entities;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Pessoa {
    private String nome;
    private String documento;
    private Endereco endereco;
}