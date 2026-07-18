package com.bmw.cine.administrador;

import com.bmw.cine.administrador.controller.AdminController;
import com.bmw.cine.administrador.view.AdminView;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Usuario;

import javafx.application.Application;
import javafx.stage.Stage;

public class AdminMain extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Usuario de prueba únicamente para ejecutar el módulo
        UsuarioDTO administrador = new UsuarioDTO(
                1,
                "Administrador",
                "admin@bmw.com",
                "admin",
                Usuario.ROL_ADMINISTRADOR,
                true
        );

        AdminView vista = new AdminView(administrador);

        new AdminController(vista, primaryStage, administrador);

        vista.mostrar(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}