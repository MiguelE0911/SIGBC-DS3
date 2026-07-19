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

public class MainWindowController {
    private final MainWindowView vista;
    private final UsuarioDTO usuarioActivo;
    private final Stage stage;
    private BilleteraController billeteraControllerActual; // null si el panel está cerrado

    public MainWindowController(MainWindowView vista, UsuarioDTO usuarioActivo, Stage stage) {
        this.vista = vista;
        this.usuarioActivo = usuarioActivo;
        this.stage = stage;
        cargarCarteleraInicial();
        inicializarEventos();
    }

    private void cargarCarteleraInicial() {
        CarteleraView carteleraView = new CarteleraView();
        // Antes: this::refrescarBilleteraSiEstaAbierta
        // Ahora: al comprar, la Billetera se ABRE (si estaba cerrada) y se refresca.
        new CarteleraController(carteleraView, stage, usuarioActivo, this::irABilletera);
        vista.setVistaCentral(carteleraView);
    }

    private void inicializarEventos() {
        vista.getBtnBilletera().setOnAction(e -> {
            BilleteraView billeteraVista = new BilleteraView();
            billeteraControllerActual = new BilleteraController(billeteraVista, usuarioActivo);
            vista.toggleBilletera(billeteraVista); // este sí alterna: botón manual = abrir/cerrar
        });

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

    /**
     * Se ejecuta tras una compra exitosa (viene encadenado desde
     * SeleccionAsientosController -> DetallePeliculaController -> CarteleraController).
     * Abre la Billetera si estaba cerrada y la deja actualizada con el boleto nuevo.
     */
    private void irABilletera() {
        BilleteraView billeteraVista = new BilleteraView();
        billeteraControllerActual = new BilleteraController(billeteraVista, usuarioActivo);
        vista.mostrarBilletera(billeteraVista); // idempotente: no cierra si ya estaba abierta
    }
}