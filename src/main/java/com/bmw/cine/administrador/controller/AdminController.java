package com.bmw.cine.administrador.controller;

import com.bmw.cine.administrador.view.AdminView;
import com.bmw.cine.administrador.view.GestionUsuariosView;
import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dao.impl.UsuarioDAOImpl;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.view.HeaderPrincipalController;

import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AdminController {

    private final AdminView vista;
    private final Stage stage;
    private final UsuarioDTO usuarioActivo;
    private final UsuarioDAO usuarioDAO;

    private Button btnGestionUsuarios;
    private Button btnReportes;

    public AdminController(AdminView vista,
                           Stage stage,
                           UsuarioDTO usuarioActivo) {

        this.vista = vista;
        this.stage = stage;
        this.usuarioActivo = usuarioActivo;

        this.usuarioDAO = new UsuarioDAOImpl();

        configurarHeader();
        configurarEventos();

        // Vista inicial
        mostrarGestionUsuarios();
        vista.getHeaderController().marcarActivo(btnGestionUsuarios);
    }

    /**
     * Configura los botones de navegación del Header.
     */
    private void configurarHeader() {

        HeaderPrincipalController header = vista.getHeaderController();

        btnGestionUsuarios = header.agregarBotonNav(
                "Gestión de Usuarios",
                this::mostrarGestionUsuarios);

        btnReportes = header.agregarBotonNav(
                "Reportes",
                () -> System.out.println("Reportes"));
    }

    /**
     * Configura las opciones del menú del usuario.
     */
    private void configurarEventos() {

        HeaderPrincipalController header = vista.getHeaderController();

        header.setOnCambiarSeccion(() -> {
            System.out.println("Cambiar de sección");
            // TODO: SelectorModulo.iniciar(stage, usuarioActivo);
        });

        header.setOnCerrarSesion(() -> {
            System.out.println("Cerrar sesión");
            // TODO: Regresar al Login
        });
    }

    /**
     * Muestra la vista de Gestión de Usuarios.
     */
    private void mostrarGestionUsuarios() {

        GestionUsuariosView gestionUsuariosView =
                new GestionUsuariosView();

        new GestionUsuariosController(
                gestionUsuariosView,
                usuarioDAO
        );

        vista.getContentPane()
                .getChildren()
                .setAll(gestionUsuariosView);
    }

}