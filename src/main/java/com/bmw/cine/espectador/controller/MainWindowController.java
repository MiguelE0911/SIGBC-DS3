package com.bmw.cine.espectador.controller;

import java.util.Optional;

import com.bmw.cine.espectador.view.MainWindowView;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class MainWindowController {
    private MainWindowView vista;

    public MainWindowController(MainWindowView vista) {
        this.vista = vista;
        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.getBtnBilletera().setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Billetera");
            alert.setHeaderText(null);
            alert.setContentText("Abriendo panel lateral de Billetera...");
            alert.showAndWait();
        });

        vista.getBtnSesion().setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cerrar Sesión");
            alert.setHeaderText("¿Desea cerrar la sesión actual?");
            alert.setContentText("Se perderán los cambios no guardados.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                System.out.println("Sesión cerrada.");
            }
        });
    }
}