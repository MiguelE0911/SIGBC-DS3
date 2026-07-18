package com.bmw.cine.personal.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

// Placeholder de la sección Cartelera-CRUD dentro del Panel de Personal.
public class CarteleraCrudView extends VBox {

    public CarteleraCrudView() {
        this.setStyle("-fx-background-color: #100b16;");

        this.setAlignment(Pos.CENTER);
        this.setSpacing(14);
        this.setPadding(new Insets(48));

        Label titulo = new Label("Cartelera — CRUD");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f4e8d0; -fx-font-family: 'Segoe UI';");

        Label subtitulo = new Label("Alta, edición y baja de películas — próximamente (Meta 4)");
        subtitulo.setStyle("-fx-font-size: 13px; -fx-text-fill: #b8a9c9; -fx-font-family: 'Segoe UI';");
        subtitulo.setWrapText(true);

        getChildren().addAll(titulo, subtitulo);
    }
}