package com.bmw.cine.personal.controller;

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
import com.bmw.cine.common.util.Notificador;
import com.bmw.cine.personal.view.GestionFuncionesView;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GestionFuncionesController {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final GestionFuncionesView vista;
    private final FuncionDAO funcionDAO = new FuncionDAOImpl();
    private final PeliculaDAO peliculaDAO = new PeliculaDAOImpl();
    private final SalaDAO salaDAO = new SalaDAOImpl();
    private Map<Integer, Pelicula> peliculasPorId;
    private Map<Integer, Sala> salasPorId;

    public GestionFuncionesController(GestionFuncionesView vista) {
        this.vista = vista;
        configurarColumnasDatos();
        vista.getBtnAgregar().setOnAction(e -> abrirFormulario(null));
        cargarDatos();
    }

    // Configuración inicial
    private void configurarColumnasDatos() {
        TableColumn<Funcion, String> colPelicula = new TableColumn<>("Película");
        colPelicula.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                nombrePelicula(d.getValue().getPeliculaId())));

        TableColumn<Funcion, String> colSala = new TableColumn<>("Sala");
        colSala.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                nombreSala(d.getValue().getSalaId())));

        TableColumn<Funcion, String> colHorario = new TableColumn<>("Horario");
        colHorario.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getHorario().format(FORMATO)));

        TableColumn<Funcion, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox contenedor = new HBox(6, btnEditar, btnEliminar);

            {
                btnEditar.getStyleClass().add("boton-secundario");
                btnEliminar.getStyleClass().add("boton-rechazar");
                btnEditar.setOnAction(e -> abrirFormulario(getTableView().getItems().get(getIndex())));
                btnEliminar.setOnAction(e -> {
                    Funcion funcion = getTableView().getItems().get(getIndex());
                    if (Notificador.confirmar("Eliminar función",
                            "¿Eliminar la función de \"" + nombrePelicula(funcion.getPeliculaId())
                                    + "\" del " + funcion.getHorario().format(FORMATO) + "?"
                                    + "\nSi ya tiene boletos vendidos, no se podrá eliminar.")) {
                        eliminar(funcion);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean vacio) {
                super.updateItem(item, vacio);
                setGraphic(vacio ? null : contenedor);
            }
        });

        // Se insertan al inicio (0,1,2) para quedar Película, Sala, Horario,
        // Precio (ya puesta por la vista), Acciones — y Acciones al final.
        vista.getTabla().getColumns().add(0, colHorario);
        vista.getTabla().getColumns().add(0, colSala);
        vista.getTabla().getColumns().add(0, colPelicula);
        vista.getTabla().getColumns().add(colAcciones);
    }

    //  Carga
    private void cargarDatos() {
        try {
            // Se recargan los mapas de nombres junto con las funciones, para
            // reflejar altas/bajas recientes de Cartelera.
            peliculasPorId = peliculaDAO.listarTodas().stream()
                    .collect(Collectors.toMap(Pelicula::getId, p -> p));
            salasPorId = salaDAO.listarTodas().stream()
                    .collect(Collectors.toMap(Sala::getId, s -> s));

            vista.getTabla().setItems(FXCollections.observableArrayList(funcionDAO.listarTodas()));
        } catch (DAOException e) {
            Notificador.error("Error", "No se pudieron cargar las funciones: " + e.getMessage());
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

        List<Pelicula> peliculas = peliculaDAO.listarTodas();
        List<Sala> salas = salaDAO.listarTodas();
        if (peliculas.isEmpty() || salas.isEmpty()) {
            Notificador.advertencia("No se puede continuar",
                    "Hace falta al menos una película (Cartelera) y una sala registrada antes de crear funciones.");
            return;
        }

        Dialog<Funcion> dialog = new Dialog<>();
        dialog.setTitle(esEdicion ? "Editar función" : "Agregar función");
        if (vista.getScene() != null) {
            dialog.initOwner(vista.getScene().getWindow());
        }
        dialog.getDialogPane().getStylesheets().clear();
        dialog.getDialogPane().getStylesheets().add(
                getClass().getResource("/css/dialogo-claro.css").toExternalForm());

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
            txtPrecio.setText(existente.precioBase().toPlainString());
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
                Notificador.advertencia("Datos incompletos", error);
                event.consume();
                return;
            }

            LocalDateTime horario = LocalDateTime.of(fecha.getValue(),
                    LocalTime.of(spnHora.getValue(), spnMinuto.getValue()));
            Pelicula peliculaElegida = comboPelicula.getValue();
            LocalDateTime fin = horario.plusMinutes(peliculaElegida.getDuracionMinutos());
            Integer idAExcluir = esEdicion ? existente.getId() : null;

            if (funcionDAO.existeSolapamiento(comboSala.getValue().getId(), horario, fin, idAExcluir)) {
                Notificador.advertencia("Horario ocupado",
                        "Ya existe otra función en \"" + comboSala.getValue().getNombre()
                                + "\" que se solapa con ese horario. Elegí otra hora o sala.");
                event.consume();
            }
        });

        dialog.setResultConverter(boton -> {
            if (boton != btnGuardarTipo) return null;

            Funcion f = esEdicion ? existente : new Funcion();
            f.setPeliculaId(comboPelicula.getValue().getId());
            f.setSalaId(comboSala.getValue().getId());
            f.setHorario(LocalDateTime.of(fecha.getValue(), LocalTime.of(spnHora.getValue(), spnMinuto.getValue())));
            f.setPrecioBase(new BigDecimal(txtPrecio.getText().trim()));
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
        boolean esNueva = funcion.getId() == 0;
        try {
            if (esNueva) {
                funcionDAO.crear(funcion);
                Notificador.exito("Función creada correctamente.");
            } else {
                funcionDAO.actualizar(funcion);
                Notificador.exito("Función actualizada correctamente.");
            }
            cargarDatos();
        } catch (DAOException e) {
            Notificador.error("Error", "No se pudo guardar la función: " + e.getMessage());
        }
    }

    // Eliminar
    private void eliminar(Funcion funcion) {
        try {
            funcionDAO.eliminar(funcion.getId());
            cargarDatos();
            Notificador.exito("Función eliminada correctamente.");
        } catch (DAOException e) {
            Notificador.advertencia("No se puede eliminar",
                    "Esta función ya tiene boletos asociados y no puede eliminarse.");
        }
    }
}