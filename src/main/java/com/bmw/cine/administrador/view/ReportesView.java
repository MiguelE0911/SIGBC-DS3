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

    public ReportesView() {

        setSpacing(20);
        setPadding(new Insets(20));
        getStyleClass().add("vista-cuerpo");

        // Título
        Label titulo = new Label("Reportes");
        titulo.getStyleClass().add("vista-titulo");

        // Subtítulo
        Label subtitulo = new Label(
                "Genera y exporta documentos con la información financiera del sistema."
        );
        subtitulo.getStyleClass().add("descripcion-pagina");

        // Tarjeta
        VBox tarjetaReporte = new VBox(20);
        tarjetaReporte.setAlignment(Pos.CENTER);
        tarjetaReporte.setPadding(new Insets(30));
        tarjetaReporte.setMaxHeight(220);
        tarjetaReporte.setPrefHeight(220);

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
        tarjetaReporte.setPrefWidth(550);
        tarjetaReporte.setMaxWidth(550);

        tarjetaReporte.getChildren().addAll(
                lblTitulo,
                lblDescripcion,
                btnGenerarPDF
        );

        StackPane contenedor = new StackPane();
        contenedor.setAlignment(Pos.TOP_CENTER);
        contenedor.getChildren().add(tarjetaReporte);

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
}
