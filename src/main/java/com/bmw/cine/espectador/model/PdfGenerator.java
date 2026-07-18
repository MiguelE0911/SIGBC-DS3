package com.bmw.cine.espectador.model; // Asegúrate que la carpeta sea 'model' en minúscula [2]

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment; // IMPORTANTE: Nueva importación para manejar el error
import com.itextpdf.layout.properties.TextAlignment;

/**
 * Utilidad para la creación de archivos PDF para boletos.
 * 
 * @author Wilma
 */
public class PdfGenerator {

    private static final DeviceRgb COLOR_DORADO = new DeviceRgb(212, 175, 55);
    private static final DeviceRgb COLOR_TEXTO_OSCURO = new DeviceRgb(27, 18, 36);

    // SOLUCIÓN: Añadimos 'IOException' a la lista de errores que este método puede lanzar
    public static void generarBoletoPdf(Boleto boleto, String rutaCarpeta) throws IOException {
        String nombreArchivo = "Boleto_" + boleto.getIdCompra() + ".pdf";
        String rutaCompleta = rutaCarpeta + File.separator + nombreArchivo;

        // El try-with-resources garantiza el cierre seguro del archivo [Java Language Spec]
        try (PdfWriter writer = new PdfWriter(rutaCompleta);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Encabezado
            document.add(new Paragraph("MULTICINES BMW")
                    .setFontColor(COLOR_DORADO)
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            
            document.add(new Paragraph("COMPROBANTE DE ENTRADA VIP")
                    .setFontColor(COLOR_TEXTO_OSCURO)
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n"));

            // Tabla de Detalles
            Table table = new Table(2);
            table.setWidth(450);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            agregarFilaDetalle(table, "PELÍCULA:", boleto.getPelicula().toUpperCase());
            agregarFilaDetalle(table, "FECHA Y HORA:", boleto.getFecha().format(formatter));
            agregarFilaDetalle(table, "ASIENTO:", boleto.getAsiento());
            agregarFilaDetalle(table, "ID DE COMPRA:", "#" + boleto.getIdCompra());
            agregarFilaDetalle(table, "ESTADO:", boleto.getEstadoTexto());

            document.add(table);

            document.add(new Paragraph("\n\nPresente este documento en la sala.\n¡Disfrute su función!")
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER));
        }
        System.out.println("PDF generado exitosamente en: " + rutaCompleta);
    }

    private static void agregarFilaDetalle(Table table, String etiqueta, String valor) {
        table.addCell(new Cell().add(new Paragraph(etiqueta))
                .setBorder(Border.NO_BORDER)
                .setBold()
                .setFontColor(COLOR_DORADO));
        
        table.addCell(new Cell().add(new Paragraph(valor))
                .setBorder(Border.NO_BORDER)
                .setFontColor(COLOR_TEXTO_OSCURO));
    }
}