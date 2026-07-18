package com.bmw.cine.espectador.controller;

import java.util.Optional;

import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.espectador.view.BilleteraView;
import com.bmw.cine.espectador.view.MainWindowView;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Controlador principal de la interfaz del Espectador.
 * Gestiona el header global y la carga dinámica de la Billetera.
 * 
 * @author Wilma
 * @version 1.1
 */
public class MainWindowController {
    private final MainWindowView vista;
    private final UsuarioDTO usuarioActivo;

    /**
     * Constructor del controlador.
     * 
     * @param vista         Instancia de la vista principal.
     * @param usuarioActivo Datos del usuario que inició sesión.
     */
    public MainWindowController(MainWindowView vista, UsuarioDTO usuarioActivo) {
        this.vista = vista;
        this.usuarioActivo = usuarioActivo;
        inicializarEventos();
    }

    private void inicializarEventos() {
        // EVENTO BILLETERA: Ahora carga el panel lateral real (Meta 3)
        vista.getBtnBilletera().setOnAction(e -> {
            // 1. Instanciamos la vista del panel lateral
            BilleteraView billeteraVista = new BilleteraView();
            
            // 2. Creamos su controlador inyectando el usuario activo para filtrar sus boletos
            new BilleteraController(billeteraVista, usuarioActivo);
            
            // 3. Lo colocamos en el lado derecho del BorderPane global
            vista.getRootLayout().setRight(billeteraVista);
        });

        // EVENTO SESIÓN: Alerta de confirmación para cerrar sesión (Meta 8)
        vista.getBtnSesion().setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cerrar Sesión");
            alert.setHeaderText("¿Desea cerrar la sesión actual?");
            alert.setContentText("Se perderán los cambios no guardados.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Aquí podrías invocar un método para regresar al LoginView
                System.out.println("Sesión de " + usuarioActivo.getNombre() + " cerrada.");
                // stage.close(); o cargar el LoginView nuevamente
            }
        });
    }
}