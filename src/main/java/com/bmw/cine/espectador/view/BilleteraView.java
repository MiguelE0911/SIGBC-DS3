package com.bmw.cine.espectador.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * Vista del panel lateral de la Billetera.
 * Contiene un área desplazable para listar los boletos del espectador.
 * 
 * @author Wilma
 * @version 1.0
 */
public class BilleteraView extends VBox {

    private VBox contenedorBoletos;
    private ScrollPane scrollPane;

    public BilleteraView() {
        configurarPanelLateral();
        inicializarComponentes();
    }

    /**
     * Configura el estilo base del panel lateral siguiendo la paleta VIP.
     */
   private void configurarPanelLateral() {
    // Se elimina setPrefWidth(350) para permitir el redimensionamiento deslizable
    this.setMinWidth(300); // Tamaño mínimo para no romper el diseño
    this.setMaxWidth(600); // Tamaño máximo para no tapar toda la pantalla
    this.setSpacing(20);
    this.setPadding(new Insets(25));
    this.setStyle(
        "-fx-background-color: linear-gradient(to bottom, #1b1224, #100b16); " +
        "-fx-border-color: #3a2b4a; " +
        "-fx-border-width: 0 0 0 1;" // Borde a la izquierda
    );
}

    private void inicializarComponentes() {
        // 1. Título de la Billetera
        Label lblTitulo = new Label("Mi Billetera");
        lblTitulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f4e8d0;");

        Label lblSubtitulo = new Label("Tus boletos comprados");
        lblSubtitulo.setStyle("-fx-font-size: 13px; -fx-text-fill: #b8a9c9;");

        // 2. Contenedor interno para los boletos (Donde el Controller inyectará los BoletoItemView)
        contenedorBoletos = new VBox(15); // Espacio entre cada boleto
        contenedorBoletos.setAlignment(Pos.TOP_CENTER);
        contenedorBoletos.setPadding(new Insets(10, 0, 10, 0));
        contenedorBoletos.setStyle("-fx-background-color: transparent;");

        // 3. ScrollPane para permitir navegación si hay muchos boletos
        scrollPane = new ScrollPane();
        scrollPane.setContent(contenedorBoletos);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        
        // Estilo para que el ScrollPane sea "invisible" y combine con el fondo
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        
        // Evitar que el VBox se estire innecesariamente
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);

        // Agregamos todo al panel principal
        this.getChildren().addAll(lblTitulo, lblSubtitulo, scrollPane);
    }

    /**
     * @return El contenedor interno donde se deben agregar los BoletoItemView.
     */
    public VBox getContenedorBoletos() {
        return contenedorBoletos;
    }
}
