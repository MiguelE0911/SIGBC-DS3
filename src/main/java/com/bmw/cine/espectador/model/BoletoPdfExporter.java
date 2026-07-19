package com.bmw.cine.espectador.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

/**
 * Genera el PDF de un boleto individual: datos de la compra + QR,
 * listo para presentar en taquilla o guardar como comprobante.
 */
public class BoletoPdfExporter {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void exportar(Boleto boleto, File destino) throws IOException {
        BufferedImage qrImage = QrGenerator.generarBufferedImage(construirContenidoQr(boleto), 220);

        try (PdfWriter writer = new PdfWriter(destino.getAbsolutePath());
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {

            document.add(new Paragraph("MULTICINES BMW")
                .setBold()
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(new DeviceRgb(212, 175, 55))); // dorado #d4af37

            document.add(new Paragraph("Boleto de entrada")
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY));

            document.add(new Paragraph("\n"));

            Table tabla = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .useAllAvailableWidth();
            agregarFila(tabla, "Orden #", String.valueOf(boleto.getIdCompra()));
            agregarFila(tabla, "Película", boleto.getPelicula());
            agregarFila(tabla, "Asiento", boleto.getAsiento());
            agregarFila(tabla, "Función", boleto.getFecha().format(FORMATO_FECHA));
            agregarFila(tabla, "Estado", boleto.getEstadoTexto());
            document.add(tabla);

            document.add(new Paragraph("\n"));

            if (qrImage != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(qrImage, "png", baos);
                Image imgQr = new Image(ImageDataFactory.create(baos.toByteArray()));
                imgQr.setHorizontalAlignment(HorizontalAlignment.CENTER);
                imgQr.setWidth(150);
                imgQr.setHeight(150);
                document.add(imgQr);
            }

            document.add(new Paragraph("Presenta este código QR en taquilla")
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY));
        }
    }

    private static void agregarFila(Table tabla, String etiqueta, String valor) {
        tabla.addCell(new Cell().add(new Paragraph(etiqueta).setBold())
            .setBorder(Border.NO_BORDER));
        tabla.addCell(new Cell().add(new Paragraph(valor))
            .setBorder(Border.NO_BORDER));
    }

    private static String construirContenidoQr(Boleto boleto) {
        return String.format(
            "MULTICINES BMW%nORDEN: #%d%nPELICULA: %s%nASIENTO: %s%nESTADO: %s",
            boleto.getIdCompra(),
            boleto.getPelicula().toUpperCase(),
            boleto.getAsiento(),
            boleto.getEstadoTexto()
        );
    }
}