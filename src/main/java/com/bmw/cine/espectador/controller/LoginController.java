package com.bmw.cine.espectador.controller;

import java.util.Optional;

import com.bmw.cine.app.SessionRouter;
import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.espectador.view.LoginView;
import com.bmw.cine.espectador.view.RegistroView;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controlador encargado de gestionar la lógica de inicio de sesión.
 * @author Wilma
 * @version 1.2
 */
public class LoginController {

    private final LoginView vista;
    private final UsuarioDAO usuarioDAO;
    private final Stage stage;

    public LoginController(LoginView vista, UsuarioDAO usuarioDAO, Stage stage) {
        this.vista = vista;
        this.usuarioDAO = usuarioDAO;
        this.stage = stage;
        configurarEventos();
    }

    private void configurarEventos() {
        // Evento botón "Ingresar"
        vista.getBtnIngresar().setOnAction(e -> procesarLogin());

        // Evento enlace "Registrarse"
        vista.getLnkRegistrarse().setOnAction(e -> {
            RegistroView registroView = new RegistroView();
            new RegistroController(registroView, usuarioDAO, stage, vista);
            registroView.mostrar(stage);
        });
    }

    private void procesarLogin() {
        String usuario = vista.getUsuario();
        String password = vista.getPassword();

        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Campos Incompletos", "Por favor, complete todos los campos requeridos.", AlertType.WARNING);
            return;
        }

        try {
            Optional<UsuarioDTO> usuarioOpt = usuarioDAO.autenticar(usuario, password);

            if (usuarioOpt.isPresent()) {
                UsuarioDTO usuarioDTO = usuarioOpt.get();
                
                // Redirección al módulo correspondiente según el rol del usuario
                SessionRouter.enrutar(stage, usuarioDTO);
                
            } else {
                mostrarAlerta("Error de Acceso", "Usuario o contraseña incorrectos.", AlertType.ERROR);
            }

        } catch (com.bmw.cine.common.dao.CuentaSuspendidaException ex) {
            mostrarAlerta("Cuenta Suspendida", "Tu cuenta se encuentra inactiva. Contacta al administrador.", AlertType.ERROR);
        } catch (com.bmw.cine.common.dao.DAOException ex) {
            mostrarAlerta("Error del Sistema", "Hubo un problema de conexión con el servidor.", AlertType.ERROR);
            ex.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}