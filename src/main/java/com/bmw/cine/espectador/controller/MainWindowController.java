package com.bmw.cine.espectador.controller;

import java.util.Optional;

import com.bmw.cine.app.SessionRouter;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.espectador.view.BilleteraView;
import com.bmw.cine.espectador.view.CarteleraView;
import com.bmw.cine.espectador.view.MainWindowView;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Controlador principal de la interfaz del Espectador.
 * Gestiona el header global, la Cartelera como vista central,
 * y la carga dinámica de la Billetera en el panel lateral.
 *
 * @author Wilma
 * @version 1.2
 */
public class MainWindowController {
    private final MainWindowView vista;
    private final UsuarioDTO usuarioActivo;
    private final Stage stage;

    public MainWindowController(MainWindowView vista, UsuarioDTO usuarioActivo, Stage stage) {
        this.vista = vista;
        this.usuarioActivo = usuarioActivo;
        this.stage = stage;
        cargarCarteleraInicial();
        inicializarEventos();
    }

    /**
     * La Cartelera es la vista central por defecto al entrar como Espectador.
     */
    private void cargarCarteleraInicial() {
        CarteleraView carteleraView = new CarteleraView();
        new CarteleraController(carteleraView, stage);
        vista.setVistaCentral(carteleraView);
    }

    private void inicializarEventos() {
        // EVENTO BILLETERA: usa el panel deslizable real del SplitPane (Meta 3)
        vista.getBtnBilletera().setOnAction(e -> {
            BilleteraView billeteraVista = new BilleteraView();
            new BilleteraController(billeteraVista, usuarioActivo);
            vista.toggleBilletera(billeteraVista);
        });

        // EVENTO SESIÓN: confirmación + regreso real al Login (Meta 8)
        vista.getBtnSesion().setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cerrar Sesión");
            alert.setHeaderText("¿Desea cerrar la sesión actual?");
            alert.setContentText("Se perderán los cambios no guardados.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                SessionRouter.cerrarSesion(stage);
            }
        });
    }
}