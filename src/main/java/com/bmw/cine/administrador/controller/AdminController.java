package com.bmw.cine.administrador.controller;

import com.bmw.cine.administrador.view.AdminView;
import com.bmw.cine.common.dto.UsuarioDTO;

import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AdminController {

    private final AdminView vista;
    private final Stage stage;
    private final UsuarioDTO usuarioActivo;

    private Button btnGestionUsuarios;
    private Button btnReportes;

    public AdminController(AdminView vista, Stage stage, UsuarioDTO usuarioActivo) {
        this.vista = vista;
        this.stage = stage;
        this.usuarioActivo = usuarioActivo;

        configurarHeader();
        configurarEventos();
    }

    /**
     * Configura los botones del Header reutilizable.
     */
    private void configurarHeader() {

        btnGestionUsuarios = vista.getHeaderController().agregarBotonNav(
                "Gestión de Usuarios",
                () -> System.out.println("Gestión de Usuarios"));

        btnReportes = vista.getHeaderController().agregarBotonNav(
                "Reportes",
                () -> System.out.println("Reportes"));

        // Al abrir el panel, la sección activa será Gestión de Usuarios.
        vista.getHeaderController().marcarActivo(btnGestionUsuarios);
    }

    /**
     * Configura las opciones del menú del usuario.
     */
    private void configurarEventos() {

        vista.getHeaderController().setOnCambiarSeccion(() -> {
            System.out.println("Cambiar de sección");
        });

        vista.getHeaderController().setOnCerrarSesion(() -> {
            System.out.println("Cerrar sesión");
        });

    }

}