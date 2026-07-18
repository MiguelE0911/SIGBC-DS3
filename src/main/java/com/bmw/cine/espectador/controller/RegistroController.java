package com.bmw.cine.espectador.controller;

import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dao.UsuarioDAO; // Asegúrate de importar el Modelo
import com.bmw.cine.common.model.Usuario;
import com.bmw.cine.espectador.view.LoginView;
import com.bmw.cine.espectador.view.RegistroView;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

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

    private void procesarRegistro() {
        String nombre = vista.getNombre();
        String correo = vista.getCorreo();
        String usuario = vista.getUsuario();
        String password = vista.getPassword();

        if (nombre.isEmpty() || correo.isEmpty() || usuario.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Campos Incompletos", "Por favor, complete todos los campos.", AlertType.WARNING);
            return;
        }

        try {
            // 1. Crear el modelo Usuario (con constructor adecuado)
          
            Usuario nuevoUsuario = new Usuario(nombre, correo, usuario, Usuario.ROL_ESPECTADOR);

            // 2. Registrar en base de datos
            usuarioDAO.registrar(nuevoUsuario, password);

            mostrarAlerta("Registro Exitoso", "¡Cuenta creada correctamente!", AlertType.INFORMATION);
            vistaLogin.mostrar(stage);

        } catch (DAOException e) {
            mostrarAlerta("Error de Registro", e.getMessage(), AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error inesperado.", AlertType.ERROR);
            e.printStackTrace();
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