package com.bmw.cine.administrador.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.MenuItem;

public class AdminView {

    // Componentes principales de la interfaz
    private BorderPane root;
    private HBox header;
    private StackPane contentPane;

    // Botones de navegación
    private Button btnGestionUsuarios;
    private Button btnReportes;

    // Menú del usuario
    private MenuButton menuUsuario;

    // Opciones del menú
    private MenuItem itemMiInformacion;
    private MenuItem itemCambiarSeccion;
    private MenuItem itemCerrarSesion;

    // Escena
    private Scene escena;

    /**
     * Constructor de la vista.
     */
    public AdminView() {
        inicializarComponentes();
    }

    /**
     * Inicializa y organiza todos los componentes visuales.
     */
    private void inicializarComponentes() {
        // Contenedor principal
        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1b1224, #100b16);");

        // Header
        header = new HBox(15);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #241a30;");

        // Botones de navegación
        btnGestionUsuarios = new Button("Gestión de Usuarios");
        btnReportes = new Button("Reportes");

        String estiloBoton =
                "-fx-background-color: #d4af37;" +
                        "-fx-text-fill: #1b1224;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;";

        btnGestionUsuarios.setStyle(estiloBoton);
        btnReportes.setStyle(estiloBoton);

        // Menú del usuario (por ahora solo el botón)
        menuUsuario = new MenuButton("Administrador");
        menuUsuario.setStyle(estiloBoton);

        itemMiInformacion = new MenuItem("Mi información");
        itemCambiarSeccion = new MenuItem("Cambiar de sección");
        itemCerrarSesion = new MenuItem("Cerrar sesión");

        menuUsuario.getItems().addAll(
                itemMiInformacion,
                itemCambiarSeccion,
                itemCerrarSesion
        );

        // Agregar componentes al Header
        header.getChildren().addAll(
                btnGestionUsuarios,
                btnReportes,
                menuUsuario
        );

        // Panel central
        contentPane = new StackPane();
        contentPane.setPadding(new Insets(30));

        // Agregar al BorderPane
        root.setTop(header);
        root.setCenter(contentPane);

        // Crear la escena
        escena = new Scene(root, 1200, 700);
    }

    /**
     * Muestra la vista.
     */
    public void mostrar(Stage stage) {
        stage.setTitle("Cinema BMW - Panel de Administrador");
        stage.setScene(escena);
        stage.setMaximized(true);
        stage.show();
    }

    public Button getBtnGestionUsuarios() {
        return btnGestionUsuarios;
    }

    public Button getBtnReportes() {
        return btnReportes;
    }

    public MenuButton getMenuUsuario() {
        return menuUsuario;
    }

    public StackPane getContentPane() {
        return contentPane;
    }

    public MenuItem getItemMiInformacion() {
        return itemMiInformacion;
    }

    public MenuItem getItemCambiarSeccion() {
        return itemCambiarSeccion;
    }

    public MenuItem getItemCerrarSesion() {
        return itemCerrarSesion;
    }
}
