package com.bmw.cine.espectador;

import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dao.impl.UsuarioDAOImpl;
import com.bmw.cine.espectador.controller.LoginController;
import com.bmw.cine.espectador.view.LoginView;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Clase lanzadora que conecta el DAO real con la vista de Login.
 */
public class EspectadorModule extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Instanciar la implementación real que conecta a MariaDB [3]
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl(); 

        // 2. Instanciar la vista
        LoginView loginView = new LoginView(); 

        // 3. Crear el controlador inyectando el DAO y el stage [2, 4]
        new LoginController(loginView, usuarioDAO, primaryStage); 

        // 4. Mostrar la ventana
        primaryStage.setTitle("Cine BMW - Iniciar Sesión");
        loginView.mostrar(primaryStage); 
    }

    public static void main(String[] args) {
        launch(args);
    }
}