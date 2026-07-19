package com.bmw.cine.personal.controller;

import com.bmw.cine.common.dao.BoletoDAO;
import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dao.FuncionDAO;
import com.bmw.cine.common.dao.PeliculaDAO;
import com.bmw.cine.common.dao.impl.BoletoDAOImpl;
import com.bmw.cine.common.dao.impl.FuncionDAOImpl;
import com.bmw.cine.common.dao.impl.PeliculaDAOImpl;
import com.bmw.cine.common.dto.FiltroSolicitudDTO;
import com.bmw.cine.common.dto.FuncionDTO;
import com.bmw.cine.common.dto.SolicitudBoletoDTO;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Pelicula;
import com.bmw.cine.common.util.Notificador;
import com.bmw.cine.personal.view.TaquillaView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaquillaController {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final TaquillaView vista;
    private final UsuarioDTO usuarioActivo;

    private final BoletoDAO boletoDAO = new BoletoDAOImpl();
    private final PeliculaDAO peliculaDAO = new PeliculaDAOImpl();
    private final FuncionDAO funcionDAO = new FuncionDAOImpl();

    public TaquillaController(TaquillaView vista, UsuarioDTO usuarioActivo) {
        this.vista = vista;
        this.usuarioActivo = usuarioActivo;

        configurarColumnaAcciones();
        configurarFiltros();
        cargarSolicitudes();
    }

    // Configuración inicial
    private void configurarColumnaAcciones() {
        TableColumn<SolicitudBoletoDTO, Void> columna = new TableColumn<>("Acciones");

        columna.setCellFactory(col -> new TableCell<>() {
            private final Button btnAprobar = new Button("Aprobar");
            private final Button btnRechazar = new Button("Rechazar");
            private final HBox contenedor = new HBox(8, btnAprobar, btnRechazar);

            {
                btnAprobar.getStyleClass().add("boton-aprobar");
                btnRechazar.getStyleClass().add("boton-rechazar");
                btnAprobar.setOnAction(e -> aprobar(getTableView().getItems().get(getIndex())));
                btnRechazar.setOnAction(e -> rechazar(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    SolicitudBoletoDTO fila = getTableView().getItems().get(getIndex());
                    boolean esPendiente = "PENDIENTE".equals(fila.getEstado());
                    btnAprobar.setDisable(!esPendiente);
                    btnRechazar.setDisable(!esPendiente);
                    setGraphic(contenedor);
                }
            }
        });

        vista.getTabla().getColumns().add(columna);
    }

    private void configurarFiltros() {
        ComboBox<Pelicula> comboPelicula = vista.getComboPelicula();
        ComboBox<FuncionDTO> comboFuncion = vista.getComboFuncion();
        ComboBox<String> comboEstado = vista.getComboEstado();

        //  Película
        List<Pelicula> peliculas = peliculaDAO.listarTodas();
        ObservableList<Pelicula> opcionesPelicula = FXCollections.observableArrayList();
        opcionesPelicula.add(null);
        opcionesPelicula.addAll(peliculas);
        comboPelicula.setItems(opcionesPelicula);

        comboPelicula.setConverter(new StringConverter<>() {
            @Override public String toString(Pelicula p) { return p == null ? "Todas las películas" : p.getTitulo(); }
            @Override public Pelicula fromString(String s) { return null; }
        });

        comboPelicula.setOnAction(e -> {
            actualizarComboFuncion(comboPelicula.getValue());
            cargarSolicitudes();
        });

        // Función
        comboFuncion.setConverter(new StringConverter<>() {
            @Override public String toString(FuncionDTO f) {
                return f == null ? "Todas las funciones" : f.getHorario().format(FORMATO_FECHA) + " · " + f.getNombreSala();
            }
            @Override public FuncionDTO fromString(String s) { return null; }
        });
        comboFuncion.setOnAction(e -> cargarSolicitudes());

        // Estado
        comboEstado.setItems(FXCollections.observableArrayList("Pendientes", "Confirmados", "Todos"));
        comboEstado.setValue("Pendientes");
        comboEstado.setOnAction(e -> cargarSolicitudes());

        // Botón actualizar
        vista.getBtnActualizar().setOnAction(e -> cargarSolicitudes());
    }

    private void actualizarComboFuncion(Pelicula peliculaSeleccionada) {
        ComboBox<FuncionDTO> comboFuncion = vista.getComboFuncion();
        ObservableList<FuncionDTO> opciones = FXCollections.observableArrayList();
        opciones.add(null);
        if (peliculaSeleccionada != null) {
            opciones.addAll(funcionDAO.listarPorPelicula(peliculaSeleccionada.getId()));
        }
        comboFuncion.setItems(opciones);
        comboFuncion.setValue(null);
    }

    // Carga y acciones
    private FiltroSolicitudDTO leerFiltroActual() {
        FiltroSolicitudDTO filtro = new FiltroSolicitudDTO();
        Pelicula pelicula = vista.getComboPelicula().getValue();
        FuncionDTO funcion = vista.getComboFuncion().getValue();
        String estado = vista.getComboEstado().getValue();

        if (pelicula != null) filtro.setPeliculaId(pelicula.getId());
        if (funcion != null) filtro.setFuncionId(funcion.getFuncionId());
        filtro.setEstado("Pendientes".equals(estado) ? "PENDIENTE" : "Confirmados".equals(estado) ? "CONFIRMADO" : null);
        return filtro;
    }

    private void cargarSolicitudes() {
        try {
            List<SolicitudBoletoDTO> solicitudes = boletoDAO.listarSolicitudes(leerFiltroActual());
            vista.getTabla().setItems(FXCollections.observableArrayList(solicitudes));
        } catch (DAOException e) {
            Notificador.error("Error", e.getMessage());
        }
    }

    private void aprobar(SolicitudBoletoDTO fila) {
        try {
            if (boletoDAO.aprobarSolicitud(fila.getBoletoId(), usuarioActivo.getId())) {
                cargarSolicitudes();
                Notificador.exito("Solicitud aprobada correctamente.");
            } else {
                Notificador.advertencia("Aviso", "Solicitud ya procesada.");
            }
        } catch (DAOException e) {
            Notificador.error("Error", e.getMessage());
        }
    }

    private void rechazar(SolicitudBoletoDTO fila) {
        try {
            if (boletoDAO.rechazarSolicitud(fila.getBoletoId())) {
                cargarSolicitudes();
                Notificador.exito("Solicitud rechazada correctamente.");
            } else {
                Notificador.advertencia("Aviso", "Solicitud ya procesada.");
            }
        } catch (DAOException e) {
            Notificador.error("Error", e.getMessage());
        }
    }
}