package com.bmw.cine.espectador.controller;

import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.model.Usuario;
import com.bmw.cine.espectador.view.LoginView;
import com.bmw.cine.espectador.view.RegistroView;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controlador encargado de gestionar el registro de nuevos usuarios.
 * @author Wilma
 * @version 1.1
 */
public class RegistroController {

    private final RegistroView vista;
    private final UsuarioDAO usuarioDAO;
    private final Stage stage;
    private final LoginView vistaLogin;

    public RegistroController(RegistroView vista, UsuarioDAO usuarioDAO, Stage stage, LoginView vistaLogin) {
        this.vista = vista;
        this.usuarioDAO = usuarioDAO;
        this.stage = stage;
        this.vistaLogin = vistaLogin;
        configurarEventos();
    }

    private void configurarEventos() {
        vista.getBtnRegistrar().setOnAction(e -> procesarRegistro());
        vista.getLnkVolverLogin().setOnAction(e -> vistaLogin.mostrar(stage));
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void procesarRegistro() {
        String nombre = vista.getNombre();
        String correo = vista.getCorreo();
        String usuario = vista.getUsuario();
        String password = vista.getPassword();

        if (nombre.isEmpty() || correo.isEmpty() || usuario.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Campos Incompletos", "Por favor, complete todos los campos.", AlertType.WARNING);
            return;
        }

        vista.getBtnRegistrar().setDisable(true);
        vista.getBtnRegistrar().setText("Registrando...");

        Task<Void> tareaRegistro = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Usuario nuevoUsuario = new Usuario(nombre, correo, usuario, Usuario.ROL_ESPECTADOR);
                usuarioDAO.registrar(nuevoUsuario, password);
                return null;
            }
        };

        tareaRegistro.setOnSucceeded(evt -> {
            restaurarBoton();
            mostrarAlerta("Registro Exitoso", "¡Cuenta creada correctamente!", AlertType.INFORMATION);
            vistaLogin.mostrar(stage);
        });

        tareaRegistro.setOnFailed(evt -> {
            restaurarBoton();
            Throwable ex = tareaRegistro.getException();

            if (ex instanceof DAOException) {
                mostrarAlerta("Error de Registro", ex.getMessage(), AlertType.ERROR);
            } else {
                mostrarAlerta("Error", "Ocurrió un error inesperado.", AlertType.ERROR);
                ex.printStackTrace();
            }
        });

        Thread hilo = new Thread(tareaRegistro);
        hilo.setDaemon(true);
        hilo.start();
    }

    private void restaurarBoton() {
        vista.getBtnRegistrar().setDisable(false);
        vista.getBtnRegistrar().setText("Registrarse");
    }

    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}