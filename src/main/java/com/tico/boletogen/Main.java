package com.tico.boletogen;

import com.tico.boletogen.builder.BuilderBB;
import com.tico.boletogen.builder.BuilderBoleto;
import com.tico.boletogen.entities.*;
import com.tico.boletogen.gen.GeradorBoleto;
import com.tico.boletogen.gen.PdfGenerator;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        try {
            BuilderBoleto builder = new BuilderBB();
            
            Pessoa beneficiario = new Pessoa();
            beneficiario.setNome("INFINET SERVICO DE COMUNICAÇÃO MULTIMIDIA EIRELI ME");
            beneficiario.setDocumento("12.191.875/0001-05");
            beneficiario.setEndereco(new Endereco(
                "R JOSE ALVES LIMA", "81", null, 
                "CENTAURO OESTE", "EUNAPOLIS", "BA", "45820970"));
            
            Pessoa pagador = new Pessoa();
            pagador.setNome("LUÍS HENRIQUE ARAÚJO COUTO");
            pagador.setDocumento("083.061.395-18");
            pagador.setEndereco(new Endereco(
                "RUA MARECHAL RONDON", "69", "CASA DOS FUNDOS", 
                "GUSMÃO", "EUNAPOLIS", "BA", "45820-000"));
            
            String agencia = "0792";
            String conta = "67839-2"; 
            String carteira = "17";
            String nossoNumero = "32128770000397337";
            
            GeradorBoleto gerador = new GeradorBoleto(builder);
            Boleto boleto = gerador.gerarBoleto(
                beneficiario,
                pagador,
                "3498858",
                LocalDate.of(2025, 6, 16),
                100.90,
                agencia,
                TipoConta.CONTA_CORRENTE,
                carteira,
                conta,
                nossoNumero
            );
            
            boleto.setLocalPagamento("Qualquer agência bancária");
            boleto.setInstrucoes("APÓS VENCIMENTO COBRAR MULTA DE R$ 2,02 E JUROS DE R$ 0,03 AO DIA");
            
            PdfGenerator.gerarBoletoPdf(boleto, "Boleto_INFORNET.pdf");
            System.out.println("Boleto gerado com sucesso!");
            
        } catch (Exception e) {
            System.err.println("Erro ao gerar boleto: " + e.getMessage());
            e.printStackTrace();
        }
    }
}