package com.bmw.cine.personal.view;

import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dao.FuncionDAO;
import com.bmw.cine.common.dao.PeliculaDAO;
import com.bmw.cine.common.dao.SalaDAO;
import com.bmw.cine.common.dao.impl.FuncionDAOImpl;
import com.bmw.cine.common.dao.impl.PeliculaDAOImpl;
import com.bmw.cine.common.dao.impl.SalaDAOImpl;
import com.bmw.cine.common.model.Funcion;
import com.bmw.cine.common.model.Pelicula;
import com.bmw.cine.common.model.Sala;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GestionFuncionesView extends BorderPane {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final FuncionDAO funcionDAO = new FuncionDAOImpl();
    private final PeliculaDAO peliculaDAO = new PeliculaDAOImpl();
    private final SalaDAO salaDAO = new SalaDAOImpl();

    private final ObservableList<Funcion> datos = FXCollections.observableArrayList();
    private final TableView<Funcion> tabla = new TableView<>();

    private Map<Integer, Pelicula> peliculasPorId;
    private Map<Integer, Sala> salasPorId;

    public GestionFuncionesView() {
        getStyleClass().add("panel-fondo");
        setPadding(new Insets(24));

        setTop(construirEncabezado());
        setCenter(construirTabla());

        cargarDatos();
    }

    //  Encabezado
    private HBox construirEncabezado() {
        Label titulo = new Label("Funciones — Gestión de horarios");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: -fx-texto-titulos;");

        Button btnAgregar = new Button("+ Agregar función");
        btnAgregar.getStyleClass().add("boton-aprobar");
        btnAgregar.setOnAction(e -> abrirFormulario(null));

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox encabezado = new HBox(12, titulo, spacer, btnAgregar);
        encabezado.setAlignment(Pos.CENTER_LEFT);
        encabezado.setPadding(new Insets(0, 0, 16, 0));
        return encabezado;
    }

    // Tabla
    private TableView<Funcion> construirTabla() {
        tabla.setItems(datos);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Funcion, String> colPelicula = new TableColumn<>("Película");
        colPelicula.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                nombrePelicula(d.getValue().getPeliculaId())));

        TableColumn<Funcion, String> colSala = new TableColumn<>("Sala");
        colSala.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                nombreSala(d.getValue().getSalaId())));

        TableColumn<Funcion, String> colHorario = new TableColumn<>("Horario");
        colHorario.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getHorario().format(FORMATO)));

        TableColumn<Funcion, BigDecimal> colPrecio = new TableColumn<>("Precio base");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioBase"));

        TableColumn<Funcion, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox contenedor = new HBox(6, btnEditar, btnEliminar);

            {
                btnEditar.getStyleClass().add("boton-secundario");
                btnEliminar.getStyleClass().add("boton-rechazar");
                btnEditar.setOnAction(e -> abrirFormulario(getTableView().getItems().get(getIndex())));
                btnEliminar.setOnAction(e -> confirmarEliminar(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean vacio) {
                super.updateItem(item, vacio);
                setGraphic(vacio ? null : contenedor);
            }
        });

        tabla.getColumns().addAll(colPelicula, colSala, colHorario, colPrecio, colAcciones);
        tabla.setPlaceholder(new Label("No hay funciones programadas"));
        return tabla;
    }

    private void cargarDatos() {
        try {
            // Se recargan los mapas de nombres junto con las funciones, para
            // reflejar altas/bajas recientes de Cartelera (Meta 4).
            peliculasPorId = peliculaDAO.listarTodas().stream()
                    .collect(Collectors.toMap(Pelicula::getId, p -> p));
            salasPorId = salaDAO.listarTodas().stream()
                    .collect(Collectors.toMap(Sala::getId, s -> s));

            datos.setAll(funcionDAO.listarTodas());
        } catch (DAOException e) {
            mostrarAlerta("Error", "No se pudieron cargar las funciones: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private String nombrePelicula(int id) {
        Pelicula p = peliculasPorId.get(id);
        return p == null ? "(eliminada)" : p.getTitulo();
    }

    private String nombreSala(int id) {
        Sala s = salasPorId.get(id);
        return s == null ? "(desconocida)" : s.getNombre();
    }

    // Formulario Agregar/Editar
    private void abrirFormulario(Funcion existente) {
        boolean esEdicion = existente != null;

        // Sin película o sin sala no se puede programar nada — cortamos antes de abrir el diálogo.
        List<Pelicula> peliculas = peliculaDAO.listarTodas();
        List<Sala> salas = salaDAO.listarTodas();
        if (peliculas.isEmpty() || salas.isEmpty()) {
            mostrarAlerta("No se puede continuar",
                    "Hace falta al menos una película (Cartelera) y una sala registrada antes de crear funciones.",
                    Alert.AlertType.WARNING);
            return;
        }

        Dialog<Funcion> dialog = new Dialog<>();
        dialog.setTitle(esEdicion ? "Editar función" : "Agregar función");
        if (getScene() != null) {
            dialog.initOwner(getScene().getWindow());
        }

        ButtonType btnGuardarTipo = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardarTipo, ButtonType.CANCEL);

        ComboBox<Pelicula> comboPelicula = new ComboBox<>(FXCollections.observableArrayList(peliculas));
        comboPelicula.setConverter(new StringConverter<>() {
            @Override public String toString(Pelicula p) { return p == null ? "" : p.getTitulo(); }
            @Override public Pelicula fromString(String s) { return null; }
        });

        ComboBox<Sala> comboSala = new ComboBox<>(FXCollections.observableArrayList(salas));
        comboSala.setConverter(new StringConverter<>() {
            @Override public String toString(Sala s) { return s == null ? "" : s.getNombre(); }
            @Override public Sala fromString(String s) { return null; }
        });

        DatePicker fecha = new DatePicker();
        Spinner<Integer> spnHora = new Spinner<>(0, 23, 18);
        Spinner<Integer> spnMinuto = new Spinner<>(0, 59, 0, 5);
        spnHora.setEditable(true);
        spnMinuto.setEditable(true);
        HBox filaHora = new HBox(6, new Label("Hora:"), spnHora, new Label("Min:"), spnMinuto);
        filaHora.setAlignment(Pos.CENTER_LEFT);

        TextField txtPrecio = new TextField();
        txtPrecio.setPromptText("Ej. 5.50");

        if (esEdicion) {
            comboPelicula.setValue(peliculasPorId.get(existente.getPeliculaId()));
            comboSala.setValue(salasPorId.get(existente.getSalaId()));
            fecha.setValue(existente.getHorario().toLocalDate());
            spnHora.getValueFactory().setValue(existente.getHorario().getHour());
            spnMinuto.getValueFactory().setValue(existente.getHorario().getMinute());
            txtPrecio.setText(existente.getPrecioBase().toPlainString());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(16, 0, 0, 0));
        grid.addRow(0, new Label("Película:"), comboPelicula);
        grid.addRow(1, new Label("Sala:"), comboSala);
        grid.addRow(2, new Label("Fecha:"), fecha);
        grid.addRow(3, new Label("Horario:"), filaHora);
        grid.addRow(4, new Label("Precio base:"), txtPrecio);

        dialog.getDialogPane().setContent(grid);

        Node botonGuardar = dialog.getDialogPane().lookupButton(btnGuardarTipo);
        botonGuardar.addEventFilter(ActionEvent.ACTION, event -> {
            String error = validarFormulario(comboPelicula, comboSala, fecha, txtPrecio);
            if (error != null) {
                mostrarAlerta("Datos incompletos", error, Alert.AlertType.WARNING);
                event.consume();
                return;
            }

            LocalDateTime horario = LocalDateTime.of(fecha.getValue(),
                    LocalTime.of(spnHora.getValue(), spnMinuto.getValue()));
            Pelicula peliculaElegida = comboPelicula.getValue();
            LocalDateTime fin = horario.plusMinutes(peliculaElegida.getDuracionMinutos());
            Integer idAExcluir = esEdicion ? existente.getId() : null;

            if (funcionDAO.existeSolapamiento(comboSala.getValue().getId(), horario, fin, idAExcluir)) {
                mostrarAlerta("Horario ocupado",
                        "Ya existe otra función en \"" + comboSala.getValue().getNombre()
                                + "\" que se solapa con ese horario. Elegí otra hora o sala.",
                        Alert.AlertType.WARNING);
                event.consume();
            }
        });

        dialog.setResultConverter(boton -> {
            if (boton != btnGuardarTipo) return null;

            Funcion f = esEdicion ? existente : new Funcion();
            f.setPeliculaId(comboPelicula.getValue().getId());
            f.setSalaId(comboSala.getValue().getId());
            f.setHorario(LocalDateTime.of(fecha.getValue(), LocalTime.of(spnHora.getValue(), spnMinuto.getValue())));
            f.setPrecio(new BigDecimal(txtPrecio.getText().trim()));
            return f;
        });

        dialog.showAndWait().ifPresent(this::guardar);
    }

    private String validarFormulario(ComboBox<Pelicula> comboPelicula, ComboBox<Sala> comboSala,
                                     DatePicker fecha, TextField txtPrecio) {
        if (comboPelicula.getValue() == null) return "Seleccioná una película.";
        if (comboSala.getValue() == null) return "Seleccioná una sala.";
        if (fecha.getValue() == null) return "Seleccioná una fecha.";
        if (comboPelicula.getValue().getDuracionMinutos() <= 0) {
            return "Esa película no tiene duración cargada — corregila en Cartelera antes de programar funciones.";
        }
        try {
            BigDecimal precio = new BigDecimal(txtPrecio.getText().trim());
            if (precio.compareTo(BigDecimal.ZERO) <= 0) return "El precio debe ser mayor a cero.";
        } catch (NumberFormatException | NullPointerException e) {
            return "El precio no es un número válido.";
        }
        return null;
    }

    private void guardar(Funcion funcion) {
        try {
            if (funcion.getId() == 0) {
                funcionDAO.crear(funcion);
            } else {
                funcionDAO.actualizar(funcion);
            }
            cargarDatos();
        } catch (DAOException e) {
            mostrarAlerta("Error", "No se pudo guardar la función: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Eliminar
    private void confirmarEliminar(Funcion funcion) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar función");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Eliminar la función de \"" + nombrePelicula(funcion.getPeliculaId())
                + "\" del " + funcion.getHorario().format(FORMATO) + "?"
                + "\nSi ya tiene boletos vendidos, no se podrá eliminar.");
        confirmacion.showAndWait().filter(b -> b == ButtonType.OK).ifPresent(b -> eliminar(funcion));
    }

    private void eliminar(Funcion funcion) {
        try {
            funcionDAO.eliminar(funcion.getId());
            cargarDatos();
        } catch (DAOException e) {
            mostrarAlerta("No se puede eliminar",
                    "Esta función ya tiene boletos asociados y no puede eliminarse.",
                    Alert.AlertType.WARNING);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}