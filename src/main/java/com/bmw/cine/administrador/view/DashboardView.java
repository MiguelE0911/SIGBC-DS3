package com.bmw.cine.administrador.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardView extends VBox {

    public DashboardView() {

        setPadding(new Insets(20));
        setSpacing(15);

        Label titulo = new Label("Dashboard");
        titulo.getStyleClass().add("vista-titulo");

        Label descripcion = new Label(
                "Resumen general del sistema."
        );
        descripcion.getStyleClass().add("descripcion-pagina");

        getChildren().addAll(
                titulo,
                descripcion
        );
    }
}
