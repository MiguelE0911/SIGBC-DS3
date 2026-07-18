package com.bmw.cine.espectador;

import com.bmw.cine.common.dao.UsuarioDAO;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controlador encargado de gestionar la lógica de negocio y los eventos
 * de la pantalla de creación de cuenta (RegistroView).
 * @author Wilma
 * @version 1.0
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
        // Evento del botón principal "Registrarse"
        vista.getBtnRegistrar().setOnAction(e -> procesarRegistro());

        // Evento del enlace para regresar al Login
        vista.getLnkVolverLogin().setOnAction(e -> {
            vistaLogin.mostrar(stage);
        });
    }

    private void procesarRegistro() {
        String nombre = vista.getNombre();
        String correo = vista.getCorreo();
        String usuario = vista.getUsuario();
        String password = vista.getPassword();

        // 1. Validar que ningún campo esté vacío
        if (nombre.isEmpty() || correo.isEmpty() || usuario.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Campos Incompletos", "Por favor, complete todos los campos para registrarse.", AlertType.WARNING);
            return;
        }

        try {
            // Nota: Aquí llamarías al método de inserción en base de datos de tu compañero si existiera en el DAO.
            // Ej: usuarioDAO.registrar(new UsuarioDTO(...));
            
            // Simulación de éxito para la presentación de tu módulo
            mostrarAlerta("Registro Exitoso", "¡Cuenta creada correctamente! Ahora puedes iniciar sesión.", AlertType.INFORMATION);
            
            // 2. Redirigir automáticamente de vuelta al Login para que pruebe su nueva cuenta
            vistaLogin.mostrar(stage);

        } catch (Exception ex) {
            mostrarAlerta("Error de Registro", "No se pudo crear la cuenta en este momento.", AlertType.ERROR);
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