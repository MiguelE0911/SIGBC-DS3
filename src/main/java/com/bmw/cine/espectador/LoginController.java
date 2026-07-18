package com.bmw.cine.espectador;

import java.util.Optional;

import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dto.UsuarioDTO;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controlador encargado de gestionar la lógica de negocio y los eventos
 * de la pantalla de inicio de sesión (LoginView).
 * * @author Wilma
 * @version 1.1
 */
public class LoginController {

    private final LoginView vista;
    private final UsuarioDAO usuarioDAO;
    private final Stage stage;

    /**
     * Constructor del controlador con los 3 parámetros requeridos.
     */
    public LoginController(LoginView vista, UsuarioDAO usuarioDAO, Stage stage) {
        this.vista = vista;
        this.usuarioDAO = usuarioDAO;
        this.stage = stage;

        // Inicializar los escuchadores de eventos
        configurarEventos();
    }

    /**
     * Asigna las acciones correspondientes a los botones y enlaces de la vista.
     */
   /**
     * Asigna las acciones correspondientes a los botones y enlaces de la vista.
     */
    private void configurarEventos() {
        // Evento cuando se hace clic en el botón "Ingresar"
        vista.getBtnIngresar().setOnAction(e -> procesarLogin());

        // EVENTO CORREGIDO PARA IR AL REGISTRO CON SU PROPIO CONTROLADOR:
        vista.getLnkRegistrarse().setOnAction(e -> {
            RegistroView registroView = new RegistroView();
            // Inicializamos el controlador del registro pasándole la vista actual de Login para poder regresar
            new RegistroController(registroView, usuarioDAO, stage, vista);
            registroView.mostrar(stage);
        });
    }
    /**
     * Captura las credenciales de la vista, maneja el Optional del DAO y gestiona excepciones.
     */
    private void procesarLogin() {
        String usuario = vista.getUsuario();
        String password = vista.getPassword();

        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Campos Incompletos", "Por favor, complete todos los campos requeridos.", AlertType.WARNING);
            return;
        }

        try {
            // SOLUCIÓN AL OPTIONAL: Capturamos el Optional y extraemos el DTO de forma segura
            Optional<UsuarioDTO> usuarioOpt = usuarioDAO.autenticar(usuario, password);

            if (usuarioOpt.isPresent()) {
                UsuarioDTO usuarioDTO = usuarioOpt.get();
                mostrarAlerta("¡Bienvenido!", "Inicio de sesión exitoso.", AlertType.INFORMATION);
                
                // NOTA: Como SessionRouter no existe en com.bmw.cine.common.app, 
                // lo dejamos comentado para no detener tu compilación.
                // com.bmw.cine.common.app.SessionRouter.enrutar(stage, usuarioDTO);
                
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

    /**
     * Helper para mostrar ventanas de diálogo.
     */
    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}