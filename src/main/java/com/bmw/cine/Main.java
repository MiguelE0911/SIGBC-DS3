package com.bmw.cine;

import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dao.impl.UsuarioDAOImpl;
import com.bmw.cine.espectador.controller.LoginController;
import com.bmw.cine.espectador.view.LoginView;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        System.out.println("Main principal");
        launch(args); // arranca JavaFX y llama a start(Stage)
    }

    @Override
    public void start(Stage stage) {
        // TODO: cuando exista el login real, reemplazar esto por LoginView.mostrar(stage)
        // y que sea el LoginController quien, al autenticar (UsuarioDAO.autenticar),
        // reciba el UsuarioDTO ya resuelto y llame a SelectorModulo.iniciar(...)
        // (o directo a EspectadorModule si el rol es Espectador).
        
        /* // Código de prueba
        UsuarioDTO usuarioPrueba = new UsuarioDTO(1, "Ana Torres", "ana@test.com", "ana.torres", Usuario.ROL_ADMINISTRADOR, true);
        SessionRouter.enrutar(stage, usuarioPrueba);
        */

        // --- LÍNEAS AÑADIDAS PARA EL CABLEADO FUNCIONAL ---
        
        // 1. Instanciamos la implementación real que conecta con MariaDB [3, 4]
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl(); 

        // 2. Instanciamos tu interfaz visual de Login [3, 5]
        LoginView loginView = new LoginView(); 

        // 3. Conexión de componentes: Inyectamos el DAO y la vista al Controlador [3, 6]
        // Esta línea es la que permite que los botones respondan a la lógica real.
        new LoginController(loginView, usuarioDAO, stage); 

        // 4. Desplegar la ventana inicial del flujo [3, 5]
        loginView.mostrar(stage);
    }
}