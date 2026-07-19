package com.bmw.cine.administrador.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ReportesView extends VBox {

    private final Button btnGenerarPDF;
    private final Button btnReporteBoletosPDF;

    public ReportesView() {

        setSpacing(20);
        setPadding(new Insets(20));
        getStyleClass().add("vista-cuerpo");

        // Título
        Label titulo = new Label("Reportes");
        titulo.getStyleClass().add("vista-titulo");

        // Subtítulo
        Label subtitulo = new Label(
                "Genera y exporta documentos con la información del sistema."
        );
        subtitulo.getStyleClass().add("descripcion-pagina");

        // ============================
        // TARJETA REPORTE FINANCIERO
        // ============================

        VBox tarjetaReporte = new VBox(20);
        tarjetaReporte.setAlignment(Pos.CENTER);
        tarjetaReporte.setPadding(new Insets(30));
        tarjetaReporte.setPrefHeight(220);
        tarjetaReporte.setMaxHeight(220);
        tarjetaReporte.setPrefWidth(550);
        tarjetaReporte.setMaxWidth(550);

        tarjetaReporte.getStyleClass().add("tarjeta-reporte");

        Label lblTitulo = new Label("Reporte Financiero");
        lblTitulo.getStyleClass().add("vista-titulo");

        Label lblDescripcion = new Label(
                "Exporta un resumen de las ganancias obtenidas por película en formato PDF."
        );
        lblDescripcion.setWrapText(true);
        lblDescripcion.getStyleClass().add("descripcion-tarjeta");

        btnGenerarPDF = new Button("Generar PDF");
        btnGenerarPDF.getStyleClass().add("boton-principal");

        tarjetaReporte.getChildren().addAll(
                lblTitulo,
                lblDescripcion,
                btnGenerarPDF
        );

        // TARJETA REPORTE OCUPACIÓN

        VBox tarjetaOcupacion = new VBox(20);
        tarjetaOcupacion.setAlignment(Pos.CENTER);
        tarjetaOcupacion.setPadding(new Insets(30));
        tarjetaOcupacion.setPrefHeight(220);
        tarjetaOcupacion.setMaxHeight(220);
        tarjetaOcupacion.setPrefWidth(550);
        tarjetaOcupacion.setMaxWidth(550);

        tarjetaOcupacion.getStyleClass().add("tarjeta-reporte");

        Label lblTituloOcupacion = new Label("Reporte de Boletos");
        lblTituloOcupacion.getStyleClass().add("vista-titulo");

        Label lblDescripcionOcupacion = new Label(
                "Exporta un resumen de los boletos vendidos por película o función."
        );
        lblDescripcionOcupacion.setWrapText(true);
        lblDescripcionOcupacion.getStyleClass().add("descripcion-tarjeta");

        btnReporteBoletosPDF = new Button("Generar PDF");
        btnReporteBoletosPDF.getStyleClass().add("boton-principal");

        tarjetaOcupacion.getChildren().addAll(
                lblTituloOcupacion,
                lblDescripcionOcupacion,
                btnReporteBoletosPDF
        );

        // Contenedor de tarjetas

        VBox tarjetas = new VBox(20);
        tarjetas.setAlignment(Pos.TOP_CENTER);

        tarjetas.getChildren().addAll(
                tarjetaReporte,
                tarjetaOcupacion
        );

        StackPane contenedor = new StackPane();
        contenedor.setAlignment(Pos.TOP_CENTER);
        contenedor.getChildren().add(tarjetas);

        VBox.setVgrow(contenedor, Priority.ALWAYS);

        getChildren().addAll(
                titulo,
                subtitulo,
                contenedor
        );
    }

    public Button getBtnGenerarPDF() {
        return btnGenerarPDF;
    }

    public Button getBtnReporteBoletosPDF() {
        return btnReporteBoletosPDF;
    }
}