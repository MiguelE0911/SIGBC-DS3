package com.bmw.cine.espectador.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.bmw.cine.common.dao.BoletoDAO;
import com.bmw.cine.common.dao.FuncionDAO;
import com.bmw.cine.common.dao.PeliculaDAO;
import com.bmw.cine.common.dao.impl.BoletoDAOImpl;
import com.bmw.cine.common.dto.FuncionDTO;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Pelicula;
import com.bmw.cine.espectador.view.DetallePeliculaView;

import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DetallePeliculaController {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final DetallePeliculaView vista;
    private final PeliculaDAO peliculaDAO;
    private final FuncionDAO funcionDAO;
    private final BoletoDAO boletoDAO;
    private final Stage ventanaDetalle;
    private final UsuarioDTO usuarioActivo;
    private final Runnable onCompraExitosa;
    private Pelicula peliculaActual;

    public DetallePeliculaController(int peliculaId, PeliculaDAO peliculaDAO, FuncionDAO funcionDAO,
                                      Stage stageOwner, UsuarioDTO usuarioActivo) {
        this(peliculaId, peliculaDAO, funcionDAO, stageOwner, usuarioActivo, null);
    }

    public DetallePeliculaController(int peliculaId, PeliculaDAO peliculaDAO, FuncionDAO funcionDAO,
                                      Stage stageOwner, UsuarioDTO usuarioActivo, Runnable onCompraExitosa) {
        this.peliculaDAO = peliculaDAO;
        this.funcionDAO = funcionDAO;
        this.boletoDAO = new BoletoDAOImpl();
        this.usuarioActivo = usuarioActivo;
        this.onCompraExitosa = onCompraExitosa;
        this.vista = new DetallePeliculaView();

        this.ventanaDetalle = new Stage();
        ventanaDetalle.initOwner(stageOwner);
        ventanaDetalle.initModality(Modality.WINDOW_MODAL);
        ventanaDetalle.initStyle(StageStyle.UNDECORATED);
        ventanaDetalle.setResizable(false);
        ventanaDetalle.setScene(new Scene(vista));

        configurarEventos();
        cargarPelicula(peliculaId);

        ventanaDetalle.show();
    }

    private void configurarEventos() {
        vista.getBtnCerrar().setOnAction(e -> ventanaDetalle.close());
        vista.getBtnComprar().setOnAction(e -> procesarCompra());
    }

    private void cargarPelicula(int peliculaId) {
        Task<Optional<Pelicula>> tarea = new Task<>() {
            @Override
            protected Optional<Pelicula> call() {
                return peliculaDAO.buscarPorId(peliculaId);
            }
        };

        tarea.setOnSucceeded(evt -> {
            Optional<Pelicula> resultado = tarea.getValue();
            if (resultado.isPresent()) {
                peliculaActual = resultado.get();
                vista.cargarPelicula(peliculaActual);
                cargarHorarios(peliculaId);
            } else {
                mostrarAlerta("Película no encontrada", "No se pudo cargar la información de esta película.", AlertType.ERROR);
                ventanaDetalle.close();
            }
        });

        tarea.setOnFailed(evt -> {
            mostrarAlerta("Error de Conexión", "No se pudo obtener el detalle de la película.", AlertType.ERROR);
            tarea.getException().printStackTrace();
            ventanaDetalle.close();
        });

        lanzar(tarea);
    }

    private void cargarHorarios(int peliculaId) {
        Task<List<FuncionDTO>> tarea = new Task<>() {
            @Override
            protected List<FuncionDTO> call() {
                return funcionDAO.listarPorPelicula(peliculaId);
            }
        };

        tarea.setOnSucceeded(evt -> {
            List<FuncionDTO> funciones = tarea.getValue();
            if (funciones.isEmpty()) {
                mostrarAlerta("Sin funciones disponibles", "Esta película no tiene horarios programados por el momento.", AlertType.INFORMATION);
            }
            vista.cargarHorarios(funciones);
        });

        tarea.setOnFailed(evt -> {
            mostrarAlerta("Error al cargar horarios", "No se pudieron obtener las funciones disponibles.", AlertType.ERROR);
            tarea.getException().printStackTrace();
        });

        lanzar(tarea);
    }

    private void procesarCompra() {
        FuncionDTO funcion = vista.getCbHorarios().getValue();
        int cantidad = vista.getSpCantidad().getValue();

        if (funcion == null) {
            mostrarAlerta("Selecciona un horario", "Por favor elige un horario antes de continuar.", AlertType.WARNING);
            return;
        }

        if (cantidad > funcion.getAsientosDisponibles()) {
            mostrarAlerta("Asientos insuficientes",
                "Solo quedan " + funcion.getAsientosDisponibles() + " asientos disponibles para este horario.",
                AlertType.WARNING);
            return;
        }

        String infoFuncion = funcion.getHorario().format(FORMATO_HORA) + " — " + funcion.getNombreSala();

        new SeleccionAsientosController(
            ventanaDetalle,
            peliculaActual.getTitulo(),
            infoFuncion,
            funcion.getFuncionId(),
            cantidad,
            usuarioActivo.getId(),
            funcionDAO,
            boletoDAO,
            onCompraExitosa,
            ventanaDetalle::close // al comprar con éxito, también se cierra el Detalle
        );
    }

    private void lanzar(Task<?> tarea) {
        Thread hilo = new Thread(tarea);
        hilo.setDaemon(true);
        hilo.start();
    }

    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}