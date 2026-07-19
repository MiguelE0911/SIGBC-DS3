package com.bmw.cine.personal.controller;

import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dao.PeliculaDAO;
import com.bmw.cine.common.dao.impl.PeliculaDAOImpl;
import com.bmw.cine.common.model.Pelicula;
import com.bmw.cine.common.util.Notificador;
import com.bmw.cine.common.util.PosterFileUtil;
import com.bmw.cine.personal.view.CarteleraCrudView;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class CarteleraCrudController {

    private final CarteleraCrudView vista;
    private final PeliculaDAO peliculaDAO = new PeliculaDAOImpl();

    public CarteleraCrudController(CarteleraCrudView vista) {
        this.vista = vista;
        configurarColumnaAcciones();
        vista.getBtnAgregar().setOnAction(e -> abrirFormulario(null));
        cargarDatos();
    }

    // Configuración inicial
    private void configurarColumnaAcciones() {
        TableColumn<Pelicula, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnOcultar = new Button();
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox contenedor = new HBox(6, btnEditar, btnOcultar, btnEliminar);

            {
                btnEditar.getStyleClass().add("boton-secundario");
                btnEliminar.getStyleClass().add("boton-rechazar");
                btnOcultar.getStyleClass().add("boton-aprobar");

                btnEditar.setOnAction(e -> abrirFormulario(getTableView().getItems().get(getIndex())));
                btnOcultar.setOnAction(e -> alternarVisibilidad(getTableView().getItems().get(getIndex())));
                btnEliminar.setOnAction(e -> {
                    Pelicula pelicula = getTableView().getItems().get(getIndex());
                    if (Notificador.confirmar("Eliminar película",
                            "¿Eliminar \"" + pelicula.getTitulo() + "\"? Esta acción no se puede deshacer.")) {
                        eliminar(pelicula);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean vacio) {
                super.updateItem(item, vacio);
                if (vacio) {
                    setGraphic(null);
                } else {
                    Pelicula p = getTableView().getItems().get(getIndex());
                    btnOcultar.setText(p.isActiva() ? "Ocultar" : "Mostrar");
                    setGraphic(contenedor);
                }
            }
        });

        vista.getTabla().getColumns().add(colAcciones);
    }

    // Carga
    private void cargarDatos() {
        try {
            List<Pelicula> lista = peliculaDAO.listarTodas();
            vista.getTabla().setItems(FXCollections.observableArrayList(lista));
        } catch (DAOException e) {
            Notificador.error("Error", "No se pudo cargar la cartelera: " + e.getMessage());
        }
    }

    //  Formulario Agregar/Editar
    private void abrirFormulario(Pelicula existente) {
        boolean esEdicion = existente != null;

        Dialog<Pelicula> dialog = new Dialog<>();
        dialog.setTitle(esEdicion ? "Editar película" : "Agregar película");
        if (vista.getScene() != null) {
            dialog.initOwner(vista.getScene().getWindow());
        }
        dialog.getDialogPane().getStylesheets().clear();
        dialog.getDialogPane().getStylesheets().add(
                getClass().getResource("/css/dialogo-claro.css").toExternalForm());

        ButtonType btnGuardarTipo = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardarTipo, ButtonType.CANCEL);

        TextField txtTitulo = new TextField(esEdicion ? existente.getTitulo() : "");
        TextField txtGenero = new TextField(esEdicion ? existente.getGenero() : "");
        Spinner<Integer> spnDuracion = new Spinner<>(1, 600, esEdicion ? existente.getDuracionMinutos() : 90);
        spnDuracion.setEditable(true);
        TextArea txtSinopsis = new TextArea(esEdicion ? existente.getSinopsis() : "");
        txtSinopsis.setPrefRowCount(3);
        txtSinopsis.setWrapText(true);

        ImageView previewPoster = new ImageView();
        previewPoster.setFitWidth(120);
        previewPoster.setFitHeight(160);
        previewPoster.setPreserveRatio(true);

        String[] rutaPoster = { esEdicion ? existente.getRutaPoster() : null };
        previewPoster.setImage(PosterFileUtil.cargarImagen(rutaPoster[0]));

        Button btnExaminar = new Button("Examinar...");
        btnExaminar.setOnAction(e -> {
            Optional<File> archivo = PosterFileUtil.elegirArchivo(dialog.getDialogPane().getScene().getWindow());
            archivo.ifPresent(f -> {
                String nombreGuardado = PosterFileUtil.guardarPoster(f);
                rutaPoster[0] = nombreGuardado;
                previewPoster.setImage(PosterFileUtil.cargarImagen(nombreGuardado));
            });
        });

        VBox posterBox = new VBox(8, previewPoster, btnExaminar);
        posterBox.setAlignment(Pos.TOP_CENTER);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(16, 0, 16, 0));
        grid.addRow(0, new Label("Título:"), txtTitulo);
        grid.addRow(1, new Label("Género:"), txtGenero);
        grid.addRow(2, new Label("Duración (min):"), spnDuracion);
        grid.addRow(3, new Label("Sinopsis:"), txtSinopsis);

        HBox contenido = new HBox(20, grid, posterBox);
        dialog.getDialogPane().setContent(contenido);

        Node botonGuardar = dialog.getDialogPane().lookupButton(btnGuardarTipo);
        botonGuardar.addEventFilter(ActionEvent.ACTION, event -> {
            if (txtTitulo.getText().isBlank()) {
                Notificador.advertencia("Campos incompletos", "El título es obligatorio.");
                event.consume();
            }
        });

        dialog.setResultConverter(boton -> {
            if (boton != btnGuardarTipo) return null;

            Pelicula p = esEdicion ? existente : new Pelicula();
            p.setTitulo(txtTitulo.getText().trim());
            p.setSinopsis(txtSinopsis.getText().trim());
            p.setGenero(txtGenero.getText().trim());
            p.setDuracionMinutos(spnDuracion.getValue());
            p.setRutaPoster(rutaPoster[0]);
            if (!esEdicion) {
                p.setActiva(true); // nueva película entra visible por defecto
            }
            return p;
        });

        dialog.showAndWait().ifPresent(this::guardar);
    }

    private void guardar(Pelicula pelicula) {
        boolean esNueva = pelicula.getId() == 0;
        try {
            if (esNueva) {
                peliculaDAO.crear(pelicula);
                Notificador.exito("Película \"" + pelicula.getTitulo() + "\" agregada correctamente.");
            } else {
                peliculaDAO.actualizar(pelicula);
                Notificador.exito("Película \"" + pelicula.getTitulo() + "\" actualizada correctamente.");
            }
            cargarDatos();
        } catch (DAOException e) {
            Notificador.error("Error", "No se pudo guardar la película: " + e.getMessage());
        }
    }

    //  Visibilidad / Eliminar
    private void alternarVisibilidad(Pelicula pelicula) {
        boolean nuevoEstado = !pelicula.isActiva();
        try {
            peliculaDAO.actualizarVisibilidad(pelicula.getId(), nuevoEstado);
            pelicula.setActiva(nuevoEstado);
            vista.getTabla().refresh();
            Notificador.exito("Película \"" + pelicula.getTitulo() + "\" ahora está " + (nuevoEstado ? "visible" : "oculta") + ".");
        } catch (DAOException e) {
            Notificador.error("Error", "No se pudo cambiar la visibilidad: " + e.getMessage());
        }
    }

    private void eliminar(Pelicula pelicula) {
        try {
            peliculaDAO.eliminar(pelicula.getId());
            cargarDatos();
            Notificador.exito("Película \"" + pelicula.getTitulo() + "\" eliminada correctamente.");
        } catch (DAOException e) {
            Notificador.advertencia("No se puede eliminar",
                    "Esta película tiene funciones programadas asociadas. "
                            + "Oculta la película en vez de eliminarla, o elimina primero sus funciones.");
        }
    }
}