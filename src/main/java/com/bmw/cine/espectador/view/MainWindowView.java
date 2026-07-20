package com.bmw.cine.espectador.view;

import com.bmw.cine.common.view.HeaderPrincipalController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

// Vista principal con SplitPane para permitir un menú lateral ajustable.
public class MainWindowView {
    private final BorderPane rootLayout;
    private final HeaderPrincipalController headerController;

    // Contenedores para el efecto deslizable
    private final SplitPane splitPane;
    private final StackPane mainContentArea;

    public MainWindowView() {
        this.rootLayout = new BorderPane();

        // HEADER COMPARTIDO (mismo componente que Personal y Administrador)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/common/view/HeaderPrincipal.fxml"));
            Parent header = loader.load();
            this.headerController = loader.getController();
            this.rootLayout.setTop(header);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar HeaderPrincipal.fxml", e);
        }

        // ÁREA CENTRAL DESLIZABLE (SplitPane)
        this.splitPane = new SplitPane();
        this.splitPane.setStyle("-fx-background-color: #100b16; -fx-box-border: transparent;");

        this.mainContentArea = new StackPane();
        this.mainContentArea.setStyle("-fx-background-color: #100b16;");

        this.splitPane.getItems().add(mainContentArea);
        this.rootLayout.setCenter(splitPane);

        // El divisor (.split-pane-divider) no existe hasta que el SplitPane
        // esté dentro de una Scene y JavaFX aplique el CSS al menos una vez.
        estilizarDivisorCuandoEsteListo();
    }

    private void estilizarDivisorCuandoEsteListo() {
        splitPane.sceneProperty().addListener((obs, escenaVieja, escenaNueva) -> {
            if (escenaNueva != null) {
                javafx.application.Platform.runLater(this::aplicarEstiloDivisor);
            }
        });
    }

    private void aplicarEstiloDivisor() {
        Node divisor = splitPane.lookup(".split-pane-divider");
        if (divisor != null) {
            divisor.setStyle("-fx-background-color: #3a2b4a;");
        }
    }

    public void mostrarBilletera(Node billeteraView) {
        if (splitPane.getItems().size() <= 1) {
            splitPane.getItems().add(billeteraView);
            splitPane.setDividerPositions(0.7);
        } else {
            splitPane.getItems().set(1, billeteraView);
        }
    }

    // Alterna la visibilidad de la billetera en el SplitPane.
    public void toggleBilletera(Node billeteraView) {
        if (splitPane.getItems().size() > 1) {
            splitPane.getItems().remove(1);
        } else {
            splitPane.getItems().add(billeteraView);
            splitPane.setDividerPositions(0.7);
        }
    }

    public void setVistaCentral(Node vista) {
        mainContentArea.getChildren().clear();
        mainContentArea.getChildren().add(vista);
    }

    public BorderPane getRootLayout() { return rootLayout; }
    public HeaderPrincipalController getHeaderController() { return headerController; }
}