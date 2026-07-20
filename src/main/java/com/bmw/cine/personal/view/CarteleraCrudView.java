package com.bmw.cine.personal.view;

import com.bmw.cine.common.model.Pelicula;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class CarteleraCrudView extends BorderPane {

    private final Button btnAgregar = new Button("+ Agregar película");
    private final TableView<Pelicula> tabla = new TableView<>();

    public CarteleraCrudView() {
        getStyleClass().add("vista-cuerpo");
        setPadding(new Insets(24));

        setTop(construirEncabezado());
        setCenter(construirTabla());
    }

    private HBox construirEncabezado() {
        Label titulo = new Label("Cartelera — Administración");
        titulo.getStyleClass().add("vista-titulo");

        btnAgregar.getStyleClass().add("boton-aprobar");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox encabezado = new HBox(12, titulo, spacer, btnAgregar);
        encabezado.setAlignment(Pos.CENTER_LEFT);
        encabezado.setPadding(new Insets(0, 0, 16, 0));
        return encabezado;
    }

    private TableView<Pelicula> construirTabla() {
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.getStyleClass().add("tabla-usuarios");

        TableColumn<Pelicula, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<Pelicula, String> colGenero = new TableColumn<>("Género");
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));

        TableColumn<Pelicula, Integer> colDuracion = new TableColumn<>("Duración (min)");
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracionMinutos"));

        TableColumn<Pelicula, Boolean> colVisible = new TableColumn<>("Visible");
        colVisible.setCellValueFactory(new PropertyValueFactory<>("activa"));
        colVisible.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean visible, boolean vacio) {
                super.updateItem(visible, vacio);
                setText(vacio || visible == null ? null : (visible ? "Sí" : "No"));
            }
        });

        tabla.getColumns().addAll(colTitulo, colGenero, colDuracion, colVisible);
        tabla.setPlaceholder(new Label("No hay películas registradas"));
        return tabla;
    }

    public Button getBtnAgregar() { return btnAgregar; }
    public TableView<Pelicula> getTabla() { return tabla; }
}