package com.bmw.cine.administrador.view;

import java.io.IOException;

import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.view.HeaderPrincipalController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AdminView {

    private UsuarioDTO usuarioActivo;  // Usuario autenticado

    // Componentes principales
    private BorderPane root;
    private StackPane contentPane;

    private HeaderPrincipalController headerController; // Header reutilizable
    private Scene escena; // Escena

     // Constructor de la vista.
    public AdminView(UsuarioDTO usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
        inicializarComponentes();
    }


     // Inicializa y organiza todos los componentes visuales.
    private void inicializarComponentes() {

        // Contenedor principal
        root = new BorderPane();
        root.getStyleClass().add("panel-fondo");


        // Header reutilizable
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/common/view/HeaderPrincipal.fxml"));

            Parent header = loader.load();

            headerController = loader.getController();

            headerController.configurar(
                    "Panel de Administrador",
                    usuarioActivo,
                    true);

            root.setTop(header);

        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar HeaderPrincipal.fxml", e);
        }

        // Contenido central
        contentPane = new StackPane();
        root.setCenter(contentPane);

        // Escena
        escena = new Scene(root, 1200, 700);

        escena.getStylesheets().add(
                getClass().getResource("/css/tema-global.css").toExternalForm());

        escena.getStylesheets().add(
                getClass().getResource("/css/header-principal.css").toExternalForm());

        escena.getStylesheets().add(
                getClass().getResource("/css/panel-comun.css").toExternalForm());

        escena.getStylesheets().add(
                getClass().getResource("/css/gestion-usuarios.css").toExternalForm());
    }

    // Muestra la vista.
    public void mostrar(Stage stage) {
        stage.setTitle("Cinema - Panel de Administrador");
        stage.setScene(escena);
        stage.setMaximized(true);
        stage.show();
    }

    // Devuelve el contenedor donde se cargarán las distintas vistas (Gestión de Usuarios, Reportes, etc.).
    public StackPane getContentPane() {
        return contentPane;
    }


    // Devuelve el controlador del Header reutilizable.
    public HeaderPrincipalController getHeaderController() {
        return headerController;
    }

     // Devuelve el usuario autenticado.
    public UsuarioDTO getUsuarioActivo() {
        return usuarioActivo;
    }
}