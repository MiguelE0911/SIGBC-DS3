package com.bmw.cine.personal.view;

import com.bmw.cine.common.dao.BoletoDAO;
import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dao.PeliculaDAO;
import com.bmw.cine.common.dao.impl.BoletoDAOImpl;
import com.bmw.cine.common.dao.impl.PeliculaDAOImpl;
import com.bmw.cine.common.dao.impl.FuncionDAOImpl;
import com.bmw.cine.common.dto.FiltroSolicitudDTO;
import com.bmw.cine.common.dto.FuncionDTO;
import com.bmw.cine.common.dto.SolicitudBoletoDTO;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Pelicula;
import com.bmw.cine.common.util.Notificador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaquillaView extends BorderPane {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final BoletoDAO boletoDAO = new BoletoDAOImpl();
    private final PeliculaDAO peliculaDAO = new PeliculaDAOImpl();
    private final FuncionDAOImpl funcionDAO = new FuncionDAOImpl(); // Instancia reutilizable

    private final UsuarioDTO usuarioActivo;
    private final ComboBox<Pelicula> comboPelicula = new ComboBox<>();
    private final ComboBox<FuncionDTO> comboFuncion = new ComboBox<>();
    private final ComboBox<String> comboEstado = new ComboBox<>();
    private final TableView<SolicitudBoletoDTO> tabla = new TableView<>();

    public TaquillaView(UsuarioDTO usuarioActivo) {
        this.usuarioActivo = usuarioActivo;

        getStyleClass().add("vista-cuerpo");
        setPadding(new Insets(24));

        setTop(construirEncabezado());
        setCenter(construirTabla());

        configurarFiltros();
        cargarSolicitudes();
    }

    private VBox construirEncabezado() {
        Label titulo = new Label("Bandeja de solicitudes");
        titulo.getStyleClass().add("vista-titulo");

        HBox filtros = new HBox(12, comboPelicula, comboFuncion, comboEstado, construirBotonActualizar());
        filtros.setAlignment(Pos.CENTER_LEFT);
        filtros.setPadding(new Insets(16, 0, 16, 0));

        comboPelicula.setPromptText("Todas las películas");
        comboFuncion.setPromptText("Todas las funciones");
        comboEstado.setPromptText("Estado");

        VBox encabezado = new VBox(8, titulo, filtros);
        return encabezado;
    }

    private Button construirBotonActualizar() {
        Button boton = new Button("Actualizar");
        boton.getStyleClass().add("boton-secundario");
        boton.setOnAction(e -> cargarSolicitudes());
        return boton;
    }

    private void configurarFiltros() {
        // --- Película ---
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
    }

    private void actualizarComboFuncion(Pelicula peliculaSeleccionada) {
        ObservableList<FuncionDTO> opciones = FXCollections.observableArrayList();
        opciones.add(null);
        if (peliculaSeleccionada != null) {
            opciones.addAll(funcionDAO.listarPorPelicula(peliculaSeleccionada.getId()));
        }
        comboFuncion.setItems(opciones);
        comboFuncion.setValue(null);
    }

    private FiltroSolicitudDTO leerFiltroActual() {
        FiltroSolicitudDTO filtro = new FiltroSolicitudDTO();
        if (comboPelicula.getValue() != null) filtro.setPeliculaId(comboPelicula.getValue().getId());
        if (comboFuncion.getValue() != null) filtro.setFuncionId(comboFuncion.getValue().getFuncionId());

        String estado = comboEstado.getValue();
        filtro.setEstado("Pendientes".equals(estado) ? "PENDIENTE" : "Confirmados".equals(estado) ? "CONFIRMADO" : null);
        return filtro;
    }

    private TableView<SolicitudBoletoDTO> construirTabla() {
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<SolicitudBoletoDTO, String> colUsuario = new TableColumn<>("Usuario");
        colUsuario.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNombreUsuario()));

        TableColumn<SolicitudBoletoDTO, String> colPelicula = new TableColumn<>("Película");
        colPelicula.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getTituloPelicula()));

        TableColumn<SolicitudBoletoDTO, String> colFuncion = new TableColumn<>("Función");
        colFuncion.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getHorarioFuncion().format(FORMATO_FECHA)));

        TableColumn<SolicitudBoletoDTO, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getEstado()));

        tabla.getColumns().addAll(colUsuario, colPelicula, colFuncion, colEstado, construirColumnaAcciones());
        tabla.setPlaceholder(new Label("No hay solicitudes con estos filtros"));

        return tabla;
    }

    private TableColumn<SolicitudBoletoDTO, Void> construirColumnaAcciones() {
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

            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); }
                else {
                    SolicitudBoletoDTO fila = getTableView().getItems().get(getIndex());
                    boolean esPendiente = "PENDIENTE".equals(fila.getEstado());
                    btnAprobar.setDisable(!esPendiente);
                    btnRechazar.setDisable(!esPendiente);
                    setGraphic(contenedor);
                }
            }
        });
        return columna;
    }

    private void cargarSolicitudes() {
        try {
            tabla.setItems(FXCollections.observableArrayList(boletoDAO.listarSolicitudes(leerFiltroActual())));
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