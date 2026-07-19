package com.bmw.cine.espectador.controller;

import java.util.List;

import com.bmw.cine.common.dao.FuncionDAO;
import com.bmw.cine.common.dao.PeliculaDAO;
import com.bmw.cine.common.dao.impl.FuncionDAOImpl;
import com.bmw.cine.common.dao.impl.PeliculaDAOImpl;
import com.bmw.cine.common.dto.PeliculaCardDTO;
import com.bmw.cine.espectador.view.CarteleraView;
import com.bmw.cine.espectador.view.PeliculaCardView;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class CarteleraController {
    private final CarteleraView vista;
    private final PeliculaDAO peliculaDAO;
    private final FuncionDAO funcionDAO;
    private final Stage stage;

    public CarteleraController(CarteleraView vista, Stage stage) {
        this.vista = vista;
        this.stage = stage;
        this.peliculaDAO = new PeliculaDAOImpl();
        this.funcionDAO = new FuncionDAOImpl();
        cargarCartelera();
    }

    private void cargarCartelera() {
        Task<List<PeliculaCardDTO>> tarea = new Task<>() {
            @Override
            protected List<PeliculaCardDTO> call() throws Exception {
                return peliculaDAO.listarCartelera();
            }
        };

        tarea.setOnSucceeded(evt -> {
            vista.getFlowPaneCartelera().getChildren().clear();
            for (PeliculaCardDTO dto : tarea.getValue()) {
                PeliculaCardView card = new PeliculaCardView(dto);
                card.setOnMouseClicked(e ->
                    new DetallePeliculaController(dto.getPeliculaId(), peliculaDAO, funcionDAO, stage)
                );
                vista.getFlowPaneCartelera().getChildren().add(card);
            }
        });

        tarea.setOnFailed(evt -> {
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error al cargar cartelera");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo obtener la lista de películas. Verifica la conexión con el servidor.");
            alerta.showAndWait();
            tarea.getException().printStackTrace();
        });

        Thread hilo = new Thread(tarea);
        hilo.setDaemon(true);
        hilo.start();
    }
}