package com.bmw.cine;

import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dao.impl.UsuarioDAOImpl;
import com.bmw.cine.espectador.controller.LoginController;
import com.bmw.cine.espectador.view.LoginView;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Único punto de entrada de la aplicación.
 * Todo el flujo arranca aquí: se instancia el DAO real, la vista de Login
 * y el controlador que los conecta.
 */
public class Main extends Application {

    public static void main(String[] args) {
        System.out.println("Iniciando aplicación Cine BMW...");
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // 1. Implementación real que conecta con MariaDB
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

        // 2. Vista de Login
        LoginView loginView = new LoginView();

        // 3. Controlador: conecta vista + DAO + stage y engancha los eventos
        new LoginController(loginView, usuarioDAO, stage);

        // 4. Mostrar la ventana
        loginView.mostrar(stage);
    }
}