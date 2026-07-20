package com.bmw.cine.espectador.controller;

import java.util.Optional;

import com.bmw.cine.app.SessionRouter;
import com.bmw.cine.common.dao.impl.UsuarioDAOImpl;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.view.HeaderPrincipalController;
import com.bmw.cine.common.view.PerfilDialog;
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
        configurarHeader();
        cargarCarteleraInicial();
    }

    private void configurarHeader() {
        HeaderPrincipalController headerCtrl = vista.getHeaderController();
        // false: el Espectador no tiene selector de módulo, entra directo a Cartelera.
        headerCtrl.configurar("🎬 Cinema BMW", usuarioActivo, false);

        headerCtrl.agregarBotonNav("💳 Billetera", this::toggleBilletera);

        headerCtrl.setOnVerPerfil(() ->
                PerfilDialog.mostrar(stage, usuarioActivo, new UsuarioDAOImpl())
                        .ifPresent(headerCtrl::actualizarNombreUsuario)
        );

        headerCtrl.setOnCerrarSesion(this::confirmarCerrarSesion);
    }

    private void confirmarCerrarSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar Sesión");
        alert.setHeaderText("¿Desea cerrar la sesión actual?");
        alert.setContentText("Se perderán los cambios no guardados.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            SessionRouter.cerrarSesion(stage);
        }
    }

    private void cargarCarteleraInicial() {
        CarteleraView carteleraView = new CarteleraView();
        new CarteleraController(carteleraView, stage, usuarioActivo, this::irABilletera);
        vista.setVistaCentral(carteleraView);
    }

    private void toggleBilletera() {
        BilleteraView billeteraVista = new BilleteraView();
        billeteraControllerActual = new BilleteraController(billeteraVista, usuarioActivo);
        vista.toggleBilletera(billeteraVista);
    }

    /**
     * Se ejecuta tras una compra exitosa. Abre la Billetera si estaba
     * cerrada y la deja actualizada con el boleto nuevo.
     */
    private void irABilletera() {
        BilleteraView billeteraVista = new BilleteraView();
        billeteraControllerActual = new BilleteraController(billeteraVista, usuarioActivo);
        vista.mostrarBilletera(billeteraVista);
    }
}