package com.tico.boletogen.gen;

import com.tico.boletogen.entities.Boleto;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public final class PdfGenerator {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private PdfGenerator() {
    }

    public static void gerarBoletoPdf(Boleto boleto, String outputPath) throws IOException {
        String html = renderHtml(boleto);

        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(fos);
            builder.run();
        }
    }

    private static String renderHtml(Boleto boleto) throws IOException {
        String template = loadTemplate("/boleto_template.html");
        if (template == null) {
            throw new IOException("Template file not found");
        }

        // Create a mutable map first
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("nomeDoBanco", boleto.getConta().getBanco().getNome());
        placeholders.put("linhaDigitavel", boleto.getLinhaDigitavel());
        placeholders.put("banco", boleto.getConta().getBanco().getNome());
        placeholders.put("codigoBanco", boleto.getConta().getBanco().getCodigo());
        placeholders.put("cedente", boleto.getBeneficiario().getNome());
        placeholders.put("cnpjCedente", boleto.getBeneficiario().getDocumento());
        placeholders.put("sacado", boleto.getPagador().getNome());
        placeholders.put("vencimento", boleto.getTitulo().getDataVencimento().format(DATE_FORMATTER));
        placeholders.put("valor", String.format("%.2f", boleto.getTitulo().getValor()));
        placeholders.put("nossoNumero", boleto.getNumero());
        placeholders.put("dataDocumento", boleto.getDataEmissao().format(DATE_FORMATTER));
        placeholders.put("dataProcessamento", LocalDate.now().format(DATE_FORMATTER));
        placeholders.put("codigoBarras", generateBarcodeBase64(boleto.getCodigoBarras()));

        return replacePlaceholders(template, placeholders);
    }

    private static String generateBarcodeBase64(String codigo) throws IOException {
        // Substituir QRCodeWriter por MultiFormatWriter e usar CODE_128
        com.google.zxing.Writer writer = new com.google.zxing.MultiFormatWriter();
        BitMatrix bitMatrix;
        try {
            bitMatrix = writer.encode(
                    codigo,
                    BarcodeFormat.CODE_128, // Formato correto para boletos bancários
                    300, // largura
                    50 // altura
            );
        } catch (com.google.zxing.WriterException e) {
            throw new IOException("Falha ao gerar código de barras", e);
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }

    private static String loadTemplate(String resourcePath) throws IOException {
        try (InputStream is = PdfGenerator.class.getResourceAsStream(resourcePath);
                Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
            return scanner.useDelimiter("\\A").next();
        }
    }

    private static String replacePlaceholders(String template, Map<String, String> values) {
        String result = template;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }
}