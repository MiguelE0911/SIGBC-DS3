package com.bmw.cine.espectador.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class CarteleraView extends VBox {
    private FlowPane flowPaneCartelera;
    private ScrollPane scrollPane;

    public CarteleraView() {
        this.setSpacing(25);
        this.setPadding(new Insets(30));
        this.setStyle("-fx-background-color: #100b16;"); // -fx-fondo-base

        Label lblSeccion = new Label("EN CARTELERA");
        lblSeccion.setStyle("-fx-text-fill: #d4af37; -fx-font-size: 24px; -fx-font-weight: bold; -fx-letter-spacing: 2px;");

        flowPaneCartelera = new FlowPane();
        flowPaneCartelera.setHgap(20);
        flowPaneCartelera.setVgap(25);
        flowPaneCartelera.setAlignment(Pos.TOP_LEFT);
        flowPaneCartelera.setStyle("-fx-background-color: transparent;");

        scrollPane = new ScrollPane(flowPaneCartelera);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        
        this.getChildren().addAll(lblSeccion, scrollPane);
    }

    public FlowPane getFlowPaneCartelera() { return flowPaneCartelera; }
}