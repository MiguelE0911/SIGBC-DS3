package com.bmw.cine.administrador.controller.InformesPDF;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.List;

public class ReporteBoletosPDF {

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

            Paragraph titulo = new Paragraph("REPORTE DE BOLETOS VENDIDOS")
                    .setBold()
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(titulo);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            Table tabla = new Table(3);
            tabla.setWidth(UnitValue.createPercentValue(100));

            tabla.addHeaderCell(new Cell().add(new Paragraph("Película")));
            tabla.addHeaderCell(new Cell().add(new Paragraph("Función")));
            tabla.addHeaderCell(new Cell().add(new Paragraph("Boletos vendidos")));

            int totalBoletos = 0;

            for (ReporteBoletosService.RegistroBoleto registro : registros) {

                tabla.addCell(registro.getPelicula());
                tabla.addCell(registro.getFuncion());
                tabla.addCell(String.valueOf(registro.getBoletosVendidos()));

                totalBoletos += registro.getBoletosVendidos();
            }

            document.add(tabla);

            document.add(new Paragraph(" "));

            document.add(
                    new Paragraph("Total de boletos vendidos: " + totalBoletos)
                            .setBold()
                            .setTextAlignment(TextAlignment.RIGHT)
            );

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}