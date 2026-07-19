package com.bmw.cine.espectador.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class SeleccionAsientosView extends VBox {

    public static final String ESTILO_DISPONIBLE =
        "-fx-background-color: #241a30; -fx-text-fill: #b8a9c9; -fx-border-color: #3a2b4a; -fx-cursor: hand;";
    public static final String ESTILO_OCUPADO =
        "-fx-background-color: #4a2020; -fx-text-fill: #7a5555; -fx-border-color: #4a2020;";
    public static final String ESTILO_SELECCIONADO =
        "-fx-background-color: #d4af37; -fx-text-fill: #1b1224; -fx-border-color: #e6c34f; -fx-cursor: hand;";

    private final Label lblTitulo;
    private final Label lblInfo;
    private final GridPane gridAsientos;
    private final ScrollPane scrollAsientos;
    private final Label lblContador;
    private final Button btnConfirmar;
    private final Button btnCancelar;
    private final Button btnCerrar;

    public SeleccionAsientosView() {
        this.setStyle("-fx-background-color: #100b16; -fx-border-color: #3a2b4a; -fx-border-width: 1;");
        this.setSpacing(14);
        this.setPadding(new Insets(20));
        this.setPrefWidth(760);
        this.setAlignment(Pos.CENTER);

        lblTitulo = new Label("Selecciona tus asientos");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #f4e8d0;");

        btnCerrar = new Button("✕");
        btnCerrar.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: #b8a9c9; -fx-font-size: 14px; -fx-cursor: hand;"
        );

        Region spacerHeader = new Region();
        HBox.setHgrow(spacerHeader, javafx.scene.layout.Priority.ALWAYS);
        HBox filaHeader = new HBox(lblTitulo, spacerHeader, btnCerrar);
        filaHeader.setAlignment(Pos.CENTER_LEFT);

        lblInfo = new Label();
        lblInfo.setStyle("-fx-font-size: 13px; -fx-text-fill: #d4af37;");

        Label lblPantalla = new Label("PANTALLA");
        lblPantalla.setStyle("-fx-text-fill: #b8a9c9; -fx-font-size: 11px; -fx-letter-spacing: 3px;");
        HBox filaPantalla = new HBox(lblPantalla);
        filaPantalla.setAlignment(Pos.CENTER);
        Region barraPantalla = new Region();
        barraPantalla.setPrefHeight(4);
        barraPantalla.setMaxWidth(400);
        barraPantalla.setStyle("-fx-background-color: #3a2b4a; -fx-background-radius: 4;");
        VBox pantalla = new VBox(6, filaPantalla, barraPantalla);
        pantalla.setAlignment(Pos.CENTER);

        gridAsientos = new GridPane();
        gridAsientos.setHgap(6);
        gridAsientos.setVgap(6);
        gridAsientos.setAlignment(Pos.CENTER);
        gridAsientos.setPadding(new Insets(4));

        scrollAsientos = new ScrollPane(gridAsientos);
        scrollAsientos.setFitToWidth(true);
        scrollAsientos.setPrefHeight(340);
        scrollAsientos.setMaxHeight(340);
        scrollAsientos.setStyle("-fx-background: #100b16; -fx-background-color: transparent;");
        scrollAsientos.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollAsientos.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        HBox leyenda = construirLeyenda();

        lblContador = new Label("Seleccionados: 0/0");
        lblContador.setStyle("-fx-text-fill: #f4e8d0; -fx-font-size: 13px; -fx-font-weight: bold;");

        btnConfirmar = new Button("Confirmar compra");
        btnConfirmar.setDisable(true);
        btnConfirmar.setStyle(
            "-fx-background-color: #d4af37; -fx-text-fill: #1b1224; -fx-font-weight: bold; " +
            "-fx-background-radius: 20; -fx-padding: 10 24 10 24; -fx-cursor: hand;"
        );

        btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: #b8a9c9; -fx-border-color: #3a2b4a; " +
            "-fx-background-radius: 20; -fx-border-radius: 20; -fx-padding: 10 24 10 24; -fx-cursor: hand;"
        );

        HBox filaBotones = new HBox(14, btnCancelar, btnConfirmar);
        filaBotones.setAlignment(Pos.CENTER);

        this.getChildren().addAll(
            filaHeader, lblInfo, pantalla, scrollAsientos, leyenda, lblContador, filaBotones
        );
    }

    private HBox construirLeyenda() {
        HBox leyenda = new HBox(20,
            itemLeyenda(ESTILO_DISPONIBLE, "Disponible"),
            itemLeyenda(ESTILO_OCUPADO, "Ocupado"),
            itemLeyenda(ESTILO_SELECCIONADO, "Seleccionado")
        );
        leyenda.setAlignment(Pos.CENTER);
        return leyenda;
    }

    private HBox itemLeyenda(String estilo, String texto) {
        Region cuadro = new Region();
        cuadro.setPrefSize(16, 16);
        cuadro.setStyle(estilo + " -fx-background-radius: 4; -fx-border-radius: 4;");
        Label lbl = new Label(texto);
        lbl.setStyle("-fx-text-fill: #b8a9c9; -fx-font-size: 11px;");
        HBox item = new HBox(6, cuadro, lbl);
        item.setAlignment(Pos.CENTER);
        return item;
    }

    public void setInfo(String tituloPelicula, String infoFuncion) {
        lblTitulo.setText("Asientos — " + tituloPelicula);
        lblInfo.setText(infoFuncion);
    }

    public void actualizarContador(int seleccionados, int requeridos) {
        lblContador.setText("Seleccionados: " + seleccionados + "/" + requeridos);
        btnConfirmar.setDisable(seleccionados != requeridos);
    }

    public GridPane getGridAsientos() { return gridAsientos; }
    public Button getBtnConfirmar() { return btnConfirmar; }
    public Button getBtnCancelar() { return btnCancelar; }
    public Button getBtnCerrar() { return btnCerrar; }
}