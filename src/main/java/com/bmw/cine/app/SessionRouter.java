package com.bmw.cine.app;

import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dao.impl.UsuarioDAOImpl;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Usuario;
import com.bmw.cine.common.session.SelectorModulo;
import com.bmw.cine.espectador.controller.LoginController;
import com.bmw.cine.espectador.view.LoginView;

import javafx.stage.Stage;

public class SessionRouter {

    public static void enrutar(Stage stage, UsuarioDTO usuarioActivo) {
        if (usuarioActivo.getRol() == Usuario.ROL_ESPECTADOR) {
            com.bmw.cine.espectador.EspectadorModule.iniciar(stage, usuarioActivo);
            return;
        }
        SelectorModulo.iniciar(stage, usuarioActivo);
    }

    public static void cerrarSesion(Stage stage) {
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        LoginView loginView = new LoginView();
        new LoginController(loginView, usuarioDAO, stage);
        loginView.mostrar(stage);
    }
}