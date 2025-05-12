// TituloPag.java
package com.tico.boletogen.entities;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TituloPag {
    private String numeroDocumento;
    private LocalDate dataVencimento;
    private double valor;
}