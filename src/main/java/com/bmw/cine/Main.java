package com.bmw.cine;

import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dao.impl.UsuarioDAOImpl;
import com.bmw.cine.espectador.controller.LoginController;
import com.bmw.cine.espectador.view.LoginView;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl(); // 1. Implementación real que conecta con MariaDB
        LoginView loginView = new LoginView(); // 2. Vista de Login
        new LoginController(loginView, usuarioDAO, stage); // 3. Controlador: conecta vista + DAO + stage y engancha los eventos
        loginView.mostrar(stage); // 4. Mostrar la ventana
    }
}