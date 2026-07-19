package com.bmw.cine.espectador.controller;

import java.util.Optional;

import com.bmw.cine.app.SessionRouter;
import com.bmw.cine.common.dao.CuentaSuspendidaException;
import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.espectador.view.LoginView;
import com.bmw.cine.espectador.view.RegistroView;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controlador encargado de gestionar la lógica de inicio de sesión.
 * @author Wilma
 * @version 1.3
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

    @SuppressWarnings("CallToPrintStackTrace")
    private void procesarLogin() {
        String usuario = vista.getUsuario();
        String password = vista.getPassword();

        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Campos Incompletos", "Por favor, complete todos los campos requeridos.", AlertType.WARNING);
            return;
        }

        // Deshabilitar el botón mientras se procesa, para evitar doble clic
        // y dar feedback visual de que la acción sí se registró.
        vista.getBtnIngresar().setDisable(true);
        vista.getBtnIngresar().setText("Ingresando...");

        // La consulta a la base de datos se ejecuta en un hilo aparte
        // para no congelar la interfaz mientras espera respuesta de MariaDB.
        Task<Optional<UsuarioDTO>> tareaLogin = new Task<>() {
            @Override
            protected Optional<UsuarioDTO> call() throws Exception {
                return usuarioDAO.autenticar(usuario, password);
            }
        };

        tareaLogin.setOnSucceeded(evt -> {
            restaurarBoton();
            Optional<UsuarioDTO> usuarioOpt = tareaLogin.getValue();

            if (usuarioOpt.isPresent()) {
                UsuarioDTO usuarioDTO = usuarioOpt.get();
                // Redirección al módulo correspondiente según el rol del usuario
                SessionRouter.enrutar(stage, usuarioDTO);
            } else {
                mostrarAlerta("Error de Acceso", "Usuario o contraseña incorrectos.", AlertType.ERROR);
            }
        });

        tareaLogin.setOnFailed(evt -> {
            restaurarBoton();
            Throwable ex = tareaLogin.getException();

            if (ex instanceof CuentaSuspendidaException) {
                mostrarAlerta("Cuenta Suspendida", "Tu cuenta se encuentra inactiva. Contacta al administrador.", AlertType.ERROR);
            } else if (ex instanceof DAOException) {
                mostrarAlerta("Error del Sistema", "Hubo un problema de conexión con el servidor.", AlertType.ERROR);
                ex.printStackTrace();
            } else {
                mostrarAlerta("Error Inesperado", "Ocurrió un error al procesar el inicio de sesión.", AlertType.ERROR);
                ex.printStackTrace();
            }
        });

        Thread hilo = new Thread(tareaLogin);
        hilo.setDaemon(true);
        hilo.start();
    }

    private void restaurarBoton() {
        vista.getBtnIngresar().setDisable(false);
        vista.getBtnIngresar().setText("Ingresar");
    }

    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}