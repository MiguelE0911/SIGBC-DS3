package com.bmw.cine.administrador.controller;

import com.bmw.cine.administrador.view.AdminView;

import javafx.stage.Stage;

public class AdminController {

    private final AdminView vista;
    private final Stage stage;

    public AdminController(AdminView vista, Stage stage) {
        this.vista = vista;
        this.stage = stage;

        configurarEventos();
    }

    /**
     * Configura todos los eventos de la vista.
     */
    private void configurarEventos() {

        vista.getBtnGestionUsuarios().setOnAction(e -> {
            System.out.println("Gestion de Usuarios");
        });

        vista.getBtnReportes().setOnAction(e -> {
            System.out.println("Reportes");
        });

        vista.getItemMiInformacion().setOnAction(e -> {
            System.out.println("Mi informacion");
        });

        vista.getItemCambiarSeccion().setOnAction(e -> {
            System.out.println("Cambiar de seccion");
        });

        vista.getItemCerrarSesion().setOnAction(e -> {
            System.out.println("Cerrar sesion");
        });

    }

}
