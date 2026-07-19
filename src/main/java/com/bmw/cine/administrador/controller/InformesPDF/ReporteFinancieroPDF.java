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

public class ReporteFinancieroPDF {

    public void generar(Window owner) {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Guardar reporte financiero");
        chooser.setInitialFileName("Reporte_Financiero.pdf");

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

            PdfWriter writer = new PdfWriter(archivo);

            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf);

            Paragraph titulo = new Paragraph("REPORTE FINANCIERO")
                    .setBold()
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(titulo);

            document.add(new Paragraph(" "));

            document.add(new Paragraph(" "));

            Table tabla = new Table(3);
            tabla.setWidth(UnitValue.createPercentValue(100));
            tabla.addHeaderCell(new Cell().add(new Paragraph("Película")));
            tabla.addHeaderCell(new Cell().add(new Paragraph("Boletos vendidos")));
            tabla.addHeaderCell(new Cell().add(new Paragraph("Ganancia")));

            // DATOS DE PRUEBA
            tabla.addCell("Superman");
            tabla.addCell("54");
            tabla.addCell("$432.00");

            tabla.addCell("Jurassic World");
            tabla.addCell("31");
            tabla.addCell("$248.00");

            tabla.addCell("Lilo & Stitch");
            tabla.addCell("73");
            tabla.addCell("$584.00");

            document.add(tabla);

            document.add(new Paragraph(" "));

            document.add(
                    new Paragraph("Total General: $1,264.00")
                            .setBold()
                            .setTextAlignment(TextAlignment.RIGHT)
            );

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}