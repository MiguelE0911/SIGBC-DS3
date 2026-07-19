package com.bmw.cine.common.view;

import com.bmw.cine.common.dto.UsuarioDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.HBox;

/**
 * Controller del header reutilizable (ver HeaderPrincipal.fxml).
 *
 * Cómo usarlo desde tu Module.iniciar():
 *
 *   FXMLLoader loader = new FXMLLoader(getClass().getResource("/common/view/HeaderPrincipal.fxml"));
 *   Parent header = loader.load();
 *   HeaderPrincipalController headerCtrl = loader.getController();
 *
 *   headerCtrl.configurar("Taquilla", usuarioActivo, true); // true = mostrar "Cambiar de sección"
 *   Button btnA = headerCtrl.agregarBotonNav("Taquilla", () -> raiz.setCenter(new TaquillaView(...)));
 *   Button btnB = headerCtrl.agregarBotonNav("Cartelera CRUD", () -> raiz.setCenter(new CarteleraCrudView()));
 *   headerCtrl.marcarActivo(btnA); // cuál sección se ve al entrar
 *
 *   headerCtrl.setOnCerrarSesion(() -> ...);
 *   headerCtrl.setOnCambiarSeccion(() -> SelectorModulo.iniciar(stage, usuarioActivo));
 */
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

    /**
     * Configura el header. Llamar justo después de cargar el FXML,
     * antes de agregar botones de navegación.
     *
     * @param tituloPanel            identificación del panel (ej. "Taquilla", "Panel de Administrador")
     * @param usuarioActivo          usuario logueado, para mostrar nombre + rol en el menú
     * @param mostrarCambiarSeccion  false para Espectador (nunca vuelve al selector); true para Personal/Admin
     */
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

    /** Marca visualmente cuál sección del header está activa. */
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