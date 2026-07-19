package com.bmw.cine.common.view;

import com.bmw.cine.common.dto.UsuarioDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.HBox;

public class HeaderPrincipalController {

    @FXML
    private Label lblTituloPanel;

    @FXML
    private HBox contenedorNav;

    @FXML
    private MenuButton menuUsuario;

    @FXML
    private MenuItem itemCambiarSeccion;

    @FXML
    private SeparatorMenuItem separadorMenu;

    @FXML
    private MenuItem itemCerrarSesion;

    @FXML
    private MenuItem itemVerPerfil;

    public void configurar(String tituloPanel, UsuarioDTO usuarioActivo, boolean mostrarCambiarSeccion) {
        lblTituloPanel.setText(tituloPanel);
        menuUsuario.setText(usuarioActivo.getNombre() + "  ·  " + usuarioActivo.getNombreRol());

        if (!mostrarCambiarSeccion) {
            menuUsuario.getItems().removeAll(itemCambiarSeccion, separadorMenu);
        }
    }

    /**
     * Agrega un botón de navegación al header (ej. "Taquilla", "Cartelera CRUD").
     * Al presionarlo, ejecuta la acción Y se marca a sí mismo como activo
     * (quita el estilo "activo" de los demás botones de navegación).
     */
    public Button agregarBotonNav(String etiqueta, Runnable accion) {
        Button boton = new Button(etiqueta);
        boton.getStyleClass().add("header-nav-boton");
        boton.setOnAction(e -> {
            accion.run();
            marcarActivo(boton);
        });
        contenedorNav.getChildren().add(boton);
        return boton;
    }

    // Marca visualmente cuál sección del header está activa.
    public void marcarActivo(Button boton) {
        contenedorNav.getChildren().forEach(n -> n.getStyleClass().remove("header-nav-boton-activo"));
        boton.getStyleClass().add("header-nav-boton-activo");
    }

    public void setOnCerrarSesion(Runnable accion) {
        itemCerrarSesion.setOnAction(e -> javafx.application.Platform.runLater(accion));
    }

    public void setOnCambiarSeccion(Runnable accion) {
        itemCambiarSeccion.setOnAction(e -> javafx.application.Platform.runLater(accion));
    }

    public void setOnVerPerfil(Runnable accion) {
        itemVerPerfil.setOnAction(e -> javafx.application.Platform.runLater(accion));
    }

    public void actualizarNombreUsuario(UsuarioDTO usuarioActualizado) { //  Refresca el nombre/rol mostrado en el menú tras editar el perfil.
        menuUsuario.setText(usuarioActualizado.getNombre() + "  ·  " + usuarioActualizado.getNombreRol());
    }
}