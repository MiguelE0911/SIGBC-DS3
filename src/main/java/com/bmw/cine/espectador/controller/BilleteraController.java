package com.bmw.cine.espectador.controller;

import java.util.List;

import com.bmw.cine.common.dao.BoletoDAO;
import com.bmw.cine.common.dao.impl.BoletoDAOImpl;
import com.bmw.cine.common.dto.SolicitudBoletoDTO;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.espectador.model.Boleto;
import com.bmw.cine.espectador.view.BilleteraView;
import com.bmw.cine.espectador.view.BoletoItemView;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controlador para gestionar la lógica de la Billetera del Espectador.
 * Consulta los boletos reales del usuario vía BoletoDAO, reutilizando
 * SolicitudBoletoDTO (mismo DTO que usa la Bandeja de Taquilla).
 *
 * @author Wilma
 * @version 1.2
 */
public class BilleteraController {

    private final BilleteraView vista;
    private final UsuarioDTO usuarioActivo;
    private final BoletoDAO boletoDAO;

    public BilleteraController(BilleteraView vista, UsuarioDTO usuarioActivo) {
        this.vista = vista;
        this.usuarioActivo = usuarioActivo;
        this.boletoDAO = new BoletoDAOImpl();

        Label lblUsuario = new Label("Billetera de: " + usuarioActivo.getNombre());
        lblUsuario.setStyle("-fx-text-fill: #f4e8d0; -fx-font-weight: bold; -fx-font-size: 14px;");
        this.vista.getContenedorBoletos().getChildren().add(lblUsuario);

        actualizarListaBoletos();
    }

    public BilleteraController(BilleteraView billeteraVista, Stage stage, UsuarioDTO usuarioActivo2) {
        // El Stage no se usa por ahora; se delega toda la inicialización
        // al constructor principal para dejar los campos final consistentes.
        this(billeteraVista, usuarioActivo2);
    }

    public void actualizarListaBoletos() {
        Task<List<SolicitudBoletoDTO>> tarea = new Task<>() {
            @Override
            protected List<SolicitudBoletoDTO> call() {
                return boletoDAO.listarPorUsuario(usuarioActivo.getId());
            }
        };

        tarea.setOnSucceeded(evt -> {
            vista.getContenedorBoletos().getChildren().removeIf(node -> node instanceof BoletoItemView);
            vista.getContenedorBoletos().getChildren().removeIf(node ->
                node instanceof Label && ((Label) node).getText().equals("No tienes boletos comprados.")
            );

            List<SolicitudBoletoDTO> dtos = tarea.getValue();
            if (dtos.isEmpty()) {
                mostrarMensajeVacio();
            } else {
                for (SolicitudBoletoDTO dto : dtos) {
                    Boleto boleto = new Boleto(
                        dto.getBoletoId(),
                        dto.getTituloPelicula(),
                        dto.getAsientoCodigo(),
                        "CONFIRMADO".equalsIgnoreCase(dto.getEstado())
                            ? Boleto.EstadoBoleto.CONFIRMADO
                            : Boleto.EstadoBoleto.PENDIENTE,
                        dto.getHorarioFuncion()
                    );
                    vista.getContenedorBoletos().getChildren().add(new BoletoItemView(boleto));
                }
            }
        });

        tarea.setOnFailed(evt -> {
            mostrarAlertaError("Error de Carga", "No se pudieron obtener tus boletos.");
            tarea.getException().printStackTrace();
        });

        Thread hilo = new Thread(tarea);
        hilo.setDaemon(true);
        hilo.start();
    }

    private void mostrarMensajeVacio() {
        Label lblVacio = new Label("No tienes boletos comprados.");
        lblVacio.setStyle("-fx-text-fill: #b8a9c9; -fx-font-style: italic;");
        vista.getContenedorBoletos().getChildren().add(lblVacio);
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}