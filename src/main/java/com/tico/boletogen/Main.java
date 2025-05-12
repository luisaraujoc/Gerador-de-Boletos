package com.tico.boletogen;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        String linhaDigitavel = "00190.00009 03212.877009 00397.337171 8 11140000010090";

        Map<String, String> dados = extrairComponentesDaLinhaDigitavel(linhaDigitavel);
        String banco = dados.get("banco");
        String moeda = dados.get("moeda");
        String fatorVenc = dados.get("fatorVenc");
        String valorNumerico = dados.get("valorNumerico");
        String campoLivre = dados.get("campoLivre");

        String codigoBarras = gerarCodigoBarras(banco, moeda, fatorVenc, valorNumerico, campoLivre);
        String barcodeBase64 = generateRealBarcode(codigoBarras);

        String htmlTemplate = lerHtml("boleto_template.html");
        htmlTemplate = htmlTemplate
                .replace("${linhaDigitavel}", linhaDigitavel)
                .replace("${banco}", banco)
                .replace("${codigoBanco}", banco) // mesma info, pode adaptar se quiser
                .replace("${valor}", formatarValorReais(valorNumerico))
                .replace("${vencimento}", fatorParaData(fatorVenc))
                .replace("${codigoBarras}", barcodeBase64)

                // Placeholders adicionais
                .replace("${cedente}", "Tico Ltda.")
                .replace("${cnpjCedente}", "12.345.678/0001-90")
                .replace("${sacado}", "Luis Henrique Araújo Couto")
                .replace("${nossoNumero}", "32128770000397336")
                .replace("${dataDocumento}", "14/08/2024")
                .replace("${dataProcessamento}", "14/08/2024");

        try (OutputStream os = new FileOutputStream("boleto.pdf")) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlTemplate, null);
            builder.toStream(os);
            builder.run();
            System.out.println("PDF gerado com sucesso!");
        }
    }

    public static Map<String, String> extrairComponentesDaLinhaDigitavel(String linha) {
        String limpa = linha.replaceAll("[.\\s]", "");
        Map<String, String> mapa = new HashMap<>();
        mapa.put("banco", limpa.substring(0, 3));
        mapa.put("moeda", limpa.substring(3, 4));
        mapa.put("fatorVenc", limpa.substring(33, 37));
        mapa.put("valorNumerico", limpa.substring(37, 47));
        mapa.put("campoLivre",
                limpa.substring(4, 9) + // parte 1
                        limpa.substring(10, 20) + // parte 2
                        limpa.substring(21, 31)); // parte 3
        return mapa;
    }

    public static String gerarCodigoBarras(String banco, String moeda, String fatorVencimento, String valor,
            String campoLivre) {
        String semDV = banco + moeda + fatorVencimento + valor + campoLivre;
        String dvGeral = calculaDigitoModulo11(semDV);
        return banco + moeda + dvGeral + fatorVencimento + valor + campoLivre;
    }

    public static String calculaDigitoModulo11(String numero) {
        int soma = 0;
        int peso = 2;
        for (int i = numero.length() - 1; i >= 0; i--) {
            int num = Character.getNumericValue(numero.charAt(i));
            soma += num * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }
        int resto = soma % 11;
        int dv = (resto == 0 || resto == 1) ? 1 : 11 - resto;
        return String.valueOf(dv);
    }

    public static String generateRealBarcode(String codigoBarras) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(codigoBarras, BarcodeFormat.ITF, 400, 80);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static String lerHtml(String nomeArquivo) throws IOException {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(nomeArquivo);
        if (inputStream == null) {
            throw new FileNotFoundException("Arquivo não encontrado no classpath: " + nomeArquivo);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder sb = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                sb.append(linha).append("\n");
            }
            return sb.toString();
        }
    }

    public static String formatarValorReais(String valorNumerico) {
        long valor = Long.parseLong(valorNumerico);
        return String.format("%.2f", valor / 100.0).replace('.', ',');
    }

    public static String fatorParaData(String fator) {
        int dias = Integer.parseInt(fator);
        LocalDate base = LocalDate.of(1997, 10, 7);
        return base.plusDays(dias).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
