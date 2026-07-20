package com.bmw.cine.espectador.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.bmw.cine.common.dao.BoletoDAO;
import com.bmw.cine.common.dao.PeliculaDAO;
import com.bmw.cine.common.dao.impl.BoletoDAOImpl;
import com.bmw.cine.common.dao.impl.PeliculaDAOImpl;
import com.bmw.cine.common.dto.SolicitudBoletoDTO;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Pelicula;
import com.bmw.cine.espectador.model.Boleto;
import com.bmw.cine.espectador.view.BilleteraView;
import com.bmw.cine.espectador.view.BoletoItemView;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class BilleteraController {

    private final BilleteraView vista;
    private final UsuarioDTO usuarioActivo;
    private final BoletoDAO boletoDAO;
    private final PeliculaDAO peliculaDAO;

    public BilleteraController(BilleteraView vista, UsuarioDTO usuarioActivo) {
        this.vista = vista;
        this.usuarioActivo = usuarioActivo;
        this.boletoDAO = new BoletoDAOImpl();
        this.peliculaDAO = new PeliculaDAOImpl();

        Label lblUsuario = new Label("Billetera de: " + usuarioActivo.getNombre());
        lblUsuario.setStyle("-fx-text-fill: #f4e8d0; -fx-font-weight: bold; -fx-font-size: 14px;");
        this.vista.getContenedorBoletos().getChildren().add(lblUsuario);

        actualizarListaBoletos();
    }

    public BilleteraController(BilleteraView billeteraVista, Stage stage, UsuarioDTO usuarioActivo2) {
        this(billeteraVista, usuarioActivo2);
    }

    public void actualizarListaBoletos() {
        Task<List<SolicitudBoletoDTO>> tarea = new Task<>() {
            @Override
            protected List<SolicitudBoletoDTO> call() throws Exception {
                List<SolicitudBoletoDTO> boletos = boletoDAO.listarPorUsuario(usuarioActivo.getId());

                // Mismas películas que Cartelera considera "ocultas" (activa = false)
                Set<String> peliculasOcultas = peliculaDAO.listarTodas().stream()
                        .filter(p -> !p.isActiva())
                        .map(Pelicula::getTitulo)
                        .collect(Collectors.toSet());

                // Se excluyen del listado, sin tocar nada en la base de datos
                return boletos.stream()
                        .filter(b -> !peliculasOcultas.contains(b.getTituloPelicula()))
                        .collect(Collectors.toList());
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