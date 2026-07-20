package com.bmw.cine.administrador.controller;

import com.bmw.cine.administrador.view.AdminView;
import com.bmw.cine.administrador.view.GestionUsuariosView;
import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dao.impl.UsuarioDAOImpl;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.view.HeaderPrincipalController;
import com.bmw.cine.administrador.view.ReportesView;
import com.bmw.cine.administrador.view.DashboardView;
import com.bmw.cine.administrador.controller.DashboardController;
import com.bmw.cine.app.SessionRouter;
import com.bmw.cine.common.session.SelectorModulo;

import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AdminController {

    private final AdminView vista;
    private final Stage stage;
    private final UsuarioDTO usuarioActivo;
    private final UsuarioDAO usuarioDAO;

    private Button btnGestionUsuarios;
    private Button btnDashboard;
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
        mostrarDashboard();
        vista.getHeaderController().marcarActivo(btnDashboard);
    }

    // Configura los botones de navegación del Header.
    private void configurarHeader() {

        HeaderPrincipalController header = vista.getHeaderController();

        btnDashboard = header.agregarBotonNav(
                "Dashboard",
                this::mostrarDashboard);

        btnGestionUsuarios = header.agregarBotonNav(
                "Gestión de Usuarios",
                this::mostrarGestionUsuarios);

        btnReportes = header.agregarBotonNav(
                "Reportes",
                this::mostrarReportes);
    }

    // Configura las opciones del menú del usuario.
    private void configurarEventos() {

        HeaderPrincipalController header = vista.getHeaderController();

        header.setOnCambiarSeccion(() -> {
            SelectorModulo.iniciar(stage, usuarioActivo);
        });

        header.setOnCerrarSesion(() -> {
            SessionRouter.cerrarSesion(stage);
        });
    }

    // Muestra la vista de Gestión de Usuarios.
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

    private void mostrarDashboard() {
        DashboardView dashboardView = new DashboardView();
        new DashboardController(dashboardView);
        vista.getContentPane()
                .getChildren()
                .setAll(dashboardView);
    }

    private void mostrarReportes() {
        ReportesView reportesView = new ReportesView();
        new ReportesController(reportesView);
        vista.getContentPane()
                .getChildren()
                .setAll(reportesView);
    }
}