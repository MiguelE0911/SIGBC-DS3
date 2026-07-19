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
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class SessionRouter {

    public static void enrutar(Stage stage, UsuarioDTO usuarioActivo) {
        if (usuarioActivo.getRol() == Usuario.ROL_ESPECTADOR) {
            MainWindowView vistaPrincipal = new MainWindowView();
            new MainWindowController(vistaPrincipal, usuarioActivo, stage);
            Scene escena = new Scene(vistaPrincipal.getRootLayout(), 1200, 800);
            stage.setScene(escena);
            stage.setTitle("Multicines BMW - Cartelera");
            stage.show();
            return;
        }
        SelectorModulo.iniciar(stage, usuarioActivo);
    }

    /**
     * Cierra la sesión activa y regresa el stage a la pantalla de Login.
     * Cualquier módulo (Personal, Administrador, Espectador) puede llamarla
     * desde su botón/menú de "Cerrar sesión" — evita duplicar la
     * instanciación de LoginView/LoginController en cada uno.
     */
    public static void cerrarSesion(Stage stage) {
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        LoginView loginView = new LoginView();
        new LoginController(loginView, usuarioDAO, stage);
        loginView.mostrar(stage);
    }

    private static void mostrarPendiente(String nombreModulo) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Módulo pendiente");
        alerta.setHeaderText(null);
        alerta.setContentText("El módulo \"" + nombreModulo + "\" aún no está implementado.");
        alerta.showAndWait();
    }
}