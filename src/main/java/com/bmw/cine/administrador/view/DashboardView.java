package com.bmw.cine.administrador.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.VBox;

public class DashboardView extends VBox {

    private final Label lblGananciasMes;
    private final Label lblPeliculaMasVendida;
    private final Label lblUsuariosActivos;

    public DashboardView() {

        setSpacing(20);
        setPadding(new Insets(20));
        getStyleClass().add("vista-cuerpo");

        Label titulo = new Label("Dashboard");
        titulo.getStyleClass().add("vista-titulo");

        Label descripcion = new Label(
                "Resumen general del sistema."
        );
        descripcion.getStyleClass().add("descripcion-pagina");

        HBox contenedorTarjetas = new HBox(20);
        contenedorTarjetas.setAlignment(Pos.CENTER);
        contenedorTarjetas.setFillHeight(false);

        VBox tarjetaGanancias = crearTarjeta(
                "Ganancias del mes"
        );
        lblGananciasMes = crearValor("$0.00");
        tarjetaGanancias.getChildren().add(lblGananciasMes);

        VBox tarjetaPelicula = crearTarjeta(
                "Película más vendida"
        );
        lblPeliculaMasVendida = crearValor("-");
        tarjetaPelicula.getChildren().add(lblPeliculaMasVendida);

        VBox tarjetaUsuarios = crearTarjeta(
                "Usuarios activos"
        );
        lblUsuariosActivos = crearValor("0");
        tarjetaUsuarios.getChildren().add(lblUsuariosActivos);

        contenedorTarjetas.getChildren().addAll(
                tarjetaGanancias,
                tarjetaPelicula,
                tarjetaUsuarios
        );

        getChildren().addAll(
                titulo,
                descripcion,
                contenedorTarjetas
        );
    }

    private VBox crearTarjeta(String titulo) {

        VBox tarjeta = new VBox(20);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setPadding(new Insets(25));

        tarjeta.setMinHeight(180);
        tarjeta.setPrefHeight(200);

        tarjeta.setPrefWidth(340);
        tarjeta.setMaxWidth(340);

        tarjeta.setFillWidth(true);
        VBox.setVgrow(tarjeta, Priority.ALWAYS);


        tarjeta.getStyleClass().add("tarjeta-reporte");

        Label lblTitulo = new Label(titulo);
        lblTitulo.getStyleClass().add("vista-titulo");

        tarjeta.getChildren().add(lblTitulo);

        return tarjeta;
    }

    private Label crearValor(String texto) {

        Label lbl = new Label(texto);

        lbl.setWrapText(true);
        lbl.setTextAlignment(TextAlignment.CENTER);
        lbl.setAlignment(Pos.CENTER);

        lbl.setMaxWidth(290);
        lbl.setPrefWidth(290);

        lbl.setStyle(
                "-fx-font-size: 36px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: -fx-acento-principal;"
        );

        return lbl;
    }

    public Label getLblGananciasMes() {
        return lblGananciasMes;
    }
    public Label getLblPeliculaMasVendida() {
        return lblPeliculaMasVendida;
    }
    public Label getLblUsuariosActivos() {
        return lblUsuariosActivos;
    }
}