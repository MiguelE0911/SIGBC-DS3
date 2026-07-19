package com.bmw.cine.espectador.controller;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bmw.cine.common.dao.BoletoDAO;
import com.bmw.cine.common.dao.FuncionDAO;
import com.bmw.cine.espectador.view.SeleccionAsientosView;

import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Controlador de Selección de Asientos (Meta 6).
 * Carga el mapa real de la sala, permite elegir exactamente `cantidad`
 * asientos, y dispara la solicitud de compra contra la base de datos.
 */
public class SeleccionAsientosController {

    private final SeleccionAsientosView vista;
    private final FuncionDAO funcionDAO;
    private final BoletoDAO boletoDAO;
    private final Stage ventanaAsientos;

    private final int usuarioId;
    private final int funcionId;
    private final int cantidadRequerida;
    private final Runnable onCompraExitosa;   // refresca Billetera si está abierta (puede ser null)
    private final Runnable onCerrarDetalle;   // cierra la ventana de Detalle de Película (puede ser null)

    private final Set<String> asientosOcupados = new LinkedHashSet<>();
    private final Set<String> asientosSeleccionados = new LinkedHashSet<>();
    private final Map<String, Button> botonesPorCodigo = new LinkedHashMap<>();

    public SeleccionAsientosController(Stage stageOwner, String tituloPelicula, String infoFuncion,
                                        int funcionId, int cantidadRequerida, int usuarioId,
                                        FuncionDAO funcionDAO, BoletoDAO boletoDAO,
                                        Runnable onCompraExitosa, Runnable onCerrarDetalle) {
        this.funcionDAO = funcionDAO;
        this.boletoDAO = boletoDAO;
        this.funcionId = funcionId;
        this.cantidadRequerida = cantidadRequerida;
        this.usuarioId = usuarioId;
        this.onCompraExitosa = onCompraExitosa;
        this.onCerrarDetalle = onCerrarDetalle;

        this.vista = new SeleccionAsientosView();
        vista.setInfo(tituloPelicula, infoFuncion);
        vista.actualizarContador(0, cantidadRequerida);

        this.ventanaAsientos = new Stage();
        ventanaAsientos.initOwner(stageOwner);
        ventanaAsientos.initModality(Modality.WINDOW_MODAL);
        ventanaAsientos.initStyle(StageStyle.UNDECORATED);
        ventanaAsientos.setResizable(false);
        ventanaAsientos.setScene(new Scene(vista));

        vista.getBtnCancelar().setOnAction(e -> ventanaAsientos.close());
        vista.getBtnConfirmar().setOnAction(e -> confirmarCompra());
        vista.getBtnCerrar().setOnAction(e -> ventanaAsientos.close());
        cargarMapaAsientos();
        ventanaAsientos.show();
        ventanaAsientos.sizeToScene();
    }

    /** Carga dimensiones de la sala + asientos ocupados, y construye el grid. */
    private void cargarMapaAsientos() {
        Task<Object[]> tarea = new Task<>() {
            @Override
            protected Object[] call() {
                int[] dimensiones = funcionDAO.obtenerDimensionesSala(funcionId);
                List<String> ocupados = boletoDAO.listarAsientosOcupados(funcionId);
                return new Object[]{dimensiones, ocupados};
            }
        };

        tarea.setOnSucceeded(evt -> {
            Object[] resultado = tarea.getValue();
            int[] dimensiones = (int[]) resultado[0];
            @SuppressWarnings("unchecked")
            List<String> ocupados = (List<String>) resultado[1];

            asientosOcupados.clear();
            asientosOcupados.addAll(ocupados);
            asientosSeleccionados.clear();

            construirGrid(dimensiones[0], dimensiones[1]);
            vista.actualizarContador(0, cantidadRequerida);
            ventanaAsientos.sizeToScene();
        });

        tarea.setOnFailed(evt -> {
            mostrarAlerta("Error", "No se pudo cargar el mapa de asientos.", AlertType.ERROR);
            tarea.getException().printStackTrace();
            ventanaAsientos.close();
        });

        lanzar(tarea);
    }

    private void construirGrid(int filas, int columnas) {
        vista.getGridAsientos().getChildren().clear();
        botonesPorCodigo.clear();

        for (int f = 0; f < filas; f++) {
            char letraFila = (char) ('A' + f);
            for (int c = 0; c < columnas; c++) {
                String codigo = letraFila + String.valueOf(c + 1);

                Button btnAsiento = new Button(codigo);
                btnAsiento.setPrefSize(46, 36);

                boolean ocupado = asientosOcupados.contains(codigo);
                btnAsiento.setDisable(ocupado);
                btnAsiento.setStyle(ocupado
                    ? SeleccionAsientosView.ESTILO_OCUPADO
                    : SeleccionAsientosView.ESTILO_DISPONIBLE);

                btnAsiento.setOnAction(e -> alternarSeleccion(codigo, btnAsiento));

                botonesPorCodigo.put(codigo, btnAsiento);
                vista.getGridAsientos().add(btnAsiento, c, f);
            }
        }
    }

    private void alternarSeleccion(String codigo, Button boton) {
        if (asientosSeleccionados.contains(codigo)) {
            asientosSeleccionados.remove(codigo);
            boton.setStyle(SeleccionAsientosView.ESTILO_DISPONIBLE);
        } else {
            if (asientosSeleccionados.size() >= cantidadRequerida) {
                mostrarAlerta("Límite alcanzado",
                    "Ya elegiste tus " + cantidadRequerida + " asiento(s). Quita uno antes de elegir otro.",
                    AlertType.INFORMATION);
                return;
            }
            asientosSeleccionados.add(codigo);
            boton.setStyle(SeleccionAsientosView.ESTILO_SELECCIONADO);
        }
        vista.actualizarContador(asientosSeleccionados.size(), cantidadRequerida);
    }

    private void confirmarCompra() {
        if (asientosSeleccionados.size() != cantidadRequerida) {
            return; // el botón ya está deshabilitado en este caso, es solo una salvaguarda
        }

        vista.getBtnConfirmar().setDisable(true);
        vista.getBtnCancelar().setDisable(true);

        List<String> seleccion = List.copyOf(asientosSeleccionados);

        Task<List<Integer>> tarea = new Task<>() {
            @Override
            protected List<Integer> call() {
                return boletoDAO.solicitarBoletos(usuarioId, funcionId, seleccion);
            }
        };

        tarea.setOnSucceeded(evt -> {
            mostrarAlerta("¡Compra realizada!",
                "Tus " + cantidadRequerida + " boleto(s) quedaron pendientes de confirmación en Taquilla. " +
                "Ya puedes verlos en tu Billetera.",
                AlertType.INFORMATION);

            if (onCompraExitosa != null) onCompraExitosa.run();
            ventanaAsientos.close();
            if (onCerrarDetalle != null) onCerrarDetalle.run();
        });

        tarea.setOnFailed(evt -> {
            // Conflicto: alguien más tomó uno de los asientos entre que se
            // cargó el mapa y se confirmó la compra.
            mostrarAlerta("Asiento ya reservado",
                "Uno o más de los asientos elegidos ya fueron reservados por otro usuario. " +
                "Actualizando el mapa, por favor elige de nuevo.",
                AlertType.WARNING);
            tarea.getException().printStackTrace();

            asientosSeleccionados.clear();
            vista.getBtnCancelar().setDisable(false);
            cargarMapaAsientos(); // refresco del mapa
        });

        lanzar(tarea);
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