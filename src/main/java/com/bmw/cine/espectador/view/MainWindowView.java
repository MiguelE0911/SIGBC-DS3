package com.bmw.cine.espectador.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainWindowView {
    private BorderPane rootLayout;
    private HBox headerContainer;
    private Label lblLogo;
    private Button btnBilletera;
    private Button btnSesion;
    private StackPane centerDynamicContainer;

    public MainWindowView() {
        rootLayout = new BorderPane();

        headerContainer = new HBox();
        headerContainer.setPadding(new Insets(15, 20, 15, 20));
        headerContainer.setStyle("-fx-background-color: #212B36;"); 
        headerContainer.setAlignment(Pos.CENTER_LEFT);
        headerContainer.setPrefHeight(65);

        lblLogo = new Label("🎬 MULTICINES BMW");
        lblLogo.setStyle("-fx-text-fill: white;");
        lblLogo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        HBox actionsContainer = new HBox(15); 
        actionsContainer.setAlignment(Pos.CENTER_RIGHT);

        btnBilletera = new Button("💳 Billetera");
        btnSesion = new Button("👤 Mi Sesión");

        String buttonStyle = "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;";
        btnBilletera.setStyle(buttonStyle);
        btnSesion.setStyle(buttonStyle);

        actionsContainer.getChildren().addAll(btnBilletera, btnSesion);
        HBox.setHgrow(actionsContainer, javafx.scene.layout.Priority.ALWAYS);
        headerContainer.getChildren().addAll(lblLogo, actionsContainer);

        rootLayout.setTop(headerContainer);

        centerDynamicContainer = new StackPane();
        centerDynamicContainer.setStyle("-fx-background-color: #FFFFFF;");
        rootLayout.setCenter(centerDynamicContainer);
    }

    public void setVistaCentral(javafx.scene.Node nuevaVista) {
        centerDynamicContainer.getChildren().clear();
        centerDynamicContainer.getChildren().add(nuevaVista);
    }

    public BorderPane getRootLayout() { return rootLayout; }
    public Button getBtnBilletera() { return btnBilletera; }
    public Button getBtnSesion() { return btnSesion; }
}