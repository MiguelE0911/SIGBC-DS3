package com.bmw.cine.espectador.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vista principal con SplitPane para permitir un menú lateral ajustable.
 * Colores VIP: Fondo #100b16, Dorado #d4af37 [3].
 */
public class MainWindowView {
    private final BorderPane rootLayout;
    private final HBox headerContainer;
    private final Label lblLogo;
    private final Button btnBilletera;
    private final Button btnSesion;
    
    // Contenedores para el efecto deslizable
    private final SplitPane splitPane;
    private final StackPane mainContentArea;

    public MainWindowView() {
        this.rootLayout = new BorderPane();
        
        // --- HEADER VIP ---
        this.headerContainer = new HBox();
        this.headerContainer.setPadding(new Insets(15, 25, 15, 25));
        this.headerContainer.setAlignment(Pos.CENTER_LEFT);
        this.headerContainer.setPrefHeight(70);
        this.headerContainer.setStyle(
            "-fx-background-color: #1b1224; " + // Fondo secundario
            "-fx-border-color: transparent transparent #3a2b4a transparent; " + // Borde sutil [4]
            "-fx-border-width: 0 0 1 0;"
        );

        this.lblLogo = new Label("🎬 MULTICINES BMW");
        this.lblLogo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        this.lblLogo.setStyle("-fx-text-fill: #f4e8d0;"); // Texto crema [3]

        String vipButtonStyle = 
            "-fx-background-color: #d4af37; -fx-text-fill: #1b1224; -fx-font-weight: bold; " +
            "-fx-background-radius: 20; -fx-padding: 8 20 8 20; -fx-cursor: hand;";

        this.btnBilletera = new Button("💳 Billetera");
        this.btnBilletera.setStyle(vipButtonStyle);

        this.btnSesion = new Button("👤 Mi Sesión");
        this.btnSesion.setStyle(vipButtonStyle);

        HBox actionsContainer = new HBox(15, btnBilletera, btnSesion);
        actionsContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(actionsContainer, Priority.ALWAYS);

        this.headerContainer.getChildren().addAll(lblLogo, actionsContainer);

        // --- ÁREA CENTRAL DESLIZABLE (SplitPane) ---
        this.splitPane = new SplitPane();
        // Estilo para ocultar el borde del SplitPane y que parezca una sola pieza
        this.splitPane.setStyle("-fx-background-color: #100b16; -fx-box-border: transparent;");

        this.mainContentArea = new StackPane();
        this.mainContentArea.setStyle("-fx-background-color: #100b16;"); // Fondo base [3]
        
        // El contenido principal es el primer elemento del SplitPane
        this.splitPane.getItems().add(mainContentArea);

        this.rootLayout.setTop(headerContainer);
        this.rootLayout.setCenter(splitPane);
        // En tu constructor de MainWindowView, después de definir el splitPane
this.splitPane.setStyle("-fx-background-color: #100b16;");

// Y añade esto para que el usuario vea dónde agarrar el divisor:
this.splitPane.lookup(".split-pane-divider").setStyle("-fx-background-color: #3a2b4a;");
    }

    /**
     * Alterna la visibilidad de la billetera en el SplitPane.
     */
    public void toggleBilletera(javafx.scene.Node billeteraView) {
        if (splitPane.getItems().size() > 1) {
            splitPane.getItems().remove(1); // Quita el panel si ya existe
        } else {
            splitPane.getItems().add(billeteraView); // Añade el panel a la derecha
            splitPane.setDividerPositions(0.7); // El contenido principal toma el 70%
        }
    }

    public void setVistaCentral(javafx.scene.Node vista) {
        mainContentArea.getChildren().clear();
        mainContentArea.getChildren().add(vista);
    }

    public BorderPane getRootLayout() { return rootLayout; }
    public Button getBtnBilletera() { return btnBilletera; }
    public Button getBtnSesion() { return btnSesion; }
}