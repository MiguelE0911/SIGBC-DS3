package com.bmw.cine.espectador;

import com.bmw.cine.espectador.controller.LoginController;
import com.bmw.cine.espectador.view.LoginView;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Clase principal lanzadora para el Módulo del Espectador.
 * Se encarga de inicializar el entorno de JavaFX y desplegar
 * la vista inicial de inicio de sesión (LoginView).
 * * @author Wilma
 * @version 1.0
 */
public class EspectadorMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Instanciamos la Vista del Login
        LoginView loginView = new LoginView();
        
        // 2. Instanciamos el DAO simulado/real para el manejo de usuarios
        com.bmw.cine.common.dao.UsuarioDAO usuarioDAO = new com.bmw.cine.common.dao.impl.UsuarioDAOImpl();
        
        // 3. Inicializamos el Controlador pasando la vista y el Stage principal
        // Esto activará de inmediato el método configurarEventos() para escuchar los botones
        new LoginController(loginView, usuarioDAO, primaryStage);
        
        // 4. Configurar el Stage y mostrar la ventana de Login obligatoriamente
        primaryStage.setTitle("Cine BMW - Iniciar Sesión");
        loginView.mostrar(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//mvn clean compile exec:java -Dexec.mainClass="com.bmw.cine.espectador.EspectadorMain"