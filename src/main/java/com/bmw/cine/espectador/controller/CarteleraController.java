package com.bmw.cine.espectador.controller;

import java.util.List;

import com.bmw.cine.common.dao.PeliculaDAO;
import com.bmw.cine.common.dao.impl.PeliculaDAOImpl;
import com.bmw.cine.common.dto.PeliculaCardDTO;
import com.bmw.cine.espectador.view.CarteleraView;
import com.bmw.cine.espectador.view.PeliculaCardView;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CarteleraController {
    private final CarteleraView vista;
    private final PeliculaDAO peliculaDAO;

    public CarteleraController(CarteleraView vista) {
        this.vista = vista;
        this.peliculaDAO = new PeliculaDAOImpl(); // Regla: Solo aquí se usa "Impl"
        cargarCartelera();
    }

    private void cargarCartelera() {
        Task<List<PeliculaCardDTO>> tarea = new Task<>() {
            @Override
            protected List<PeliculaCardDTO> call() throws Exception {
                // Obtiene solo películas con 'activa = true' del DAO
                return peliculaDAO.listarCartelera();
            }
        };

        tarea.setOnSucceeded(evt -> {
            List<PeliculaCardDTO> listaPeliculas = tarea.getValue();

            vista.getFlowPaneCartelera().getChildren().clear();
            for (PeliculaCardDTO dto : listaPeliculas) {
                PeliculaCardView card = new PeliculaCardView(dto);

                // Evento para Meta 5: Detalle de película
                card.setOnMouseClicked(e -> System.out.println("Cargando detalle de: " + dto.getTitulo()));

                vista.getFlowPaneCartelera().getChildren().add(card);
            }
        });

        tarea.setOnFailed(evt -> {
            Throwable ex = tarea.getException();
            System.err.println("Error al cargar cartelera: " + ex.getMessage());

            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error al cargar cartelera");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo obtener la lista de películas. Verifica la conexión con el servidor.");
            alerta.showAndWait();

            ex.printStackTrace();
        });

        Thread hilo = new Thread(tarea);
        hilo.setDaemon(true);
        hilo.start();
    }
}