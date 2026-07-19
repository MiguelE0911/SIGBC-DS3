package com.bmw.cine.administrador.controller.InformesPDF;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReporteBoletosPDF {

    private static final DeviceRgb MORADO = new DeviceRgb(36, 26, 48);
    private static final DeviceRgb DORADO = new DeviceRgb(212, 175, 55);
    private static final DeviceRgb GRIS = new DeviceRgb(245, 245, 245);

    public void generar(Window owner) {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Guardar reporte de boletos");
        chooser.setInitialFileName("Reporte_Boletos.pdf");

        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Documento PDF",
                        "*.pdf")
        );

        File archivo = chooser.showSaveDialog(owner);

        if (archivo == null) {
            return;
        }

        try {

            ReporteBoletosService service = new ReporteBoletosService();

            List<ReporteBoletosService.RegistroBoleto> registros =
                    service.obtenerReporte();

            PdfWriter writer = new PdfWriter(archivo);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.setMargins(40, 40, 40, 40);

            // ENCABEZADO
            Paragraph empresa = new Paragraph("CINEMA BMW")
                    .setFontSize(24)
                    .setBold()
                    .setFontColor(MORADO)
                    .setTextAlignment(TextAlignment.CENTER);

            Paragraph titulo = new Paragraph("REPORTE DE BOLETOS VENDIDOS")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);

            Paragraph fecha = new Paragraph(
                    "Generado: " +
                            LocalDateTime.now().format(
                                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                    .setFontSize(10)
                    .setFontColor(ColorConstants.DARK_GRAY)
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(empresa);
            document.add(titulo);
            document.add(fecha);

            document.add(new Paragraph(""));

            document.add(
                    new LineSeparator(
                            new SolidLine(1))
            );

            document.add(new Paragraph(""));

            // TABLA
            Table tabla = new Table(
                    UnitValue.createPercentArray(new float[]{
                            5, 3, 2
                    }));

            tabla.setWidth(UnitValue.createPercentValue(100));

            tabla.addHeaderCell(crearHeader("Película"));
            tabla.addHeaderCell(crearHeader("Función"));
            tabla.addHeaderCell(crearHeader("Boletos"));

            int totalBoletos = 0;
            boolean alternar = false;

            for (ReporteBoletosService.RegistroBoleto registro : registros) {

                DeviceRgb colorFila =
                        alternar ? GRIS : (DeviceRgb) ColorConstants.WHITE;

                tabla.addCell(
                        crearCelda(
                                registro.getPelicula(),
                                colorFila,
                                TextAlignment.LEFT));

                tabla.addCell(
                        crearCelda(
                                registro.getFuncion(),
                                colorFila,
                                TextAlignment.CENTER));

                tabla.addCell(
                        crearCelda(
                                String.valueOf(registro.getBoletosVendidos()),
                                colorFila,
                                TextAlignment.CENTER));

                totalBoletos += registro.getBoletosVendidos();

                alternar = !alternar;
            }

            document.add(tabla);

            document.add(new Paragraph(""));

            // RESUMEN
            Table resumen = new Table(1);
            resumen.setWidth(220);
            resumen.setHorizontalAlignment(HorizontalAlignment.RIGHT);

            resumen.addCell(
                    new Cell()
                            .setBackgroundColor(DORADO)
                            .setBorder(Border.NO_BORDER)
                            .add(
                                    new Paragraph("TOTAL DE BOLETOS")
                                            .setBold()
                                            .setTextAlignment(TextAlignment.CENTER)
                            )
            );

            resumen.addCell(
                    new Cell()
                            .setBorder(Border.NO_BORDER)
                            .setPadding(12)
                            .add(
                                    new Paragraph(String.valueOf(totalBoletos))
                                            .setBold()
                                            .setFontSize(18)
                                            .setTextAlignment(TextAlignment.CENTER)
                            )
            );

            document.add(resumen);

            document.add(new Paragraph(""));

            document.add(
                    new LineSeparator(
                            new SolidLine(1))
            );

            document.add(
                    new Paragraph(
                            "Reporte generado automáticamente por Cinema BMW.")
                            .setFontSize(9)
                            .setFontColor(ColorConstants.GRAY)
                            .setTextAlignment(TextAlignment.CENTER)
            );

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Cell crearHeader(String texto) {

        return new Cell()
                .setBackgroundColor(MORADO)
                .setFontColor(ColorConstants.WHITE)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .add(new Paragraph(texto));
    }

    private Cell crearCelda(
            String texto,
            DeviceRgb fondo,
            TextAlignment alineacion) {

        return new Cell()
                .setBackgroundColor(fondo)
                .setTextAlignment(alineacion)
                .add(new Paragraph(texto));
    }
}