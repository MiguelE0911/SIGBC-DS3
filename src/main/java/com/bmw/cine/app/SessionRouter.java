package com.bmw.cine.app;

import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dao.impl.UsuarioDAOImpl;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Usuario;
import com.bmw.cine.common.session.SelectorModulo;
import com.bmw.cine.espectador.controller.LoginController;
import com.bmw.cine.espectador.controller.MainWindowController;
import com.bmw.cine.espectador.view.LoginView;
import com.bmw.cine.espectador.view.MainWindowView;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SessionRouter {

    public static void enrutar(Stage stage, UsuarioDTO usuarioActivo) {
       if (usuarioActivo.getRol() == Usuario.ROL_ESPECTADOR) {
            MainWindowView vistaPrincipal = new MainWindowView();
            new MainWindowController(vistaPrincipal, usuarioActivo, stage);
            Scene escena = new Scene(vistaPrincipal.getRootLayout(), 1200, 800);

            escena.getStylesheets().addAll(
                MainWindowView.class.getResource("/css/tema-global.css").toExternalForm(),
                MainWindowView.class.getResource("/css/header-principal.css").toExternalForm()
            );

            stage.setScene(escena);
            stage.setTitle("Cinema BMW - Cartelera");
            stage.show();
            return;
       }

        SelectorModulo.iniciar(stage, usuarioActivo); // Para Personal y Administrador, se muestra el selector de módulo.
    }

    public static void cerrarSesion(Stage stage) {
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        LoginView loginView = new LoginView();
        new LoginController(loginView, usuarioDAO, stage);
        loginView.mostrar(stage);
    }
}