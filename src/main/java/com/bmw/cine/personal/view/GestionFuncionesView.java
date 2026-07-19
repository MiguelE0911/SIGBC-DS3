package com.bmw.cine.personal.view;

import com.bmw.cine.common.model.Funcion;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.math.BigDecimal;

/**
 * Vista de Gestión de funciones/horarios — solo construcción de
 * componentes visuales. Toda la lógica (carga de datos, diálogo de
 * alta/edición, validación de solapamiento, eliminación) vive en
 * GestionFuncionesController.
 */
public class GestionFuncionesView extends BorderPane {

    private final Button btnAgregar = new Button("+ Agregar función");
    private final TableView<Funcion> tabla = new TableView<>();

    public GestionFuncionesView() {
        getStyleClass().add("panel-fondo");
        setPadding(new Insets(24));

        setTop(construirEncabezado());
        setCenter(construirTabla());
    }

    private HBox construirEncabezado() {
        Label titulo = new Label("Funciones — Gestión de horarios");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: -fx-texto-titulos;");

        btnAgregar.getStyleClass().add("boton-aprobar");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox encabezado = new HBox(12, titulo, spacer, btnAgregar);
        encabezado.setAlignment(Pos.CENTER_LEFT);
        encabezado.setPadding(new Insets(0, 0, 16, 0));
        return encabezado;
    }

    private TableView<Funcion> construirTabla() {
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Funcion, BigDecimal> colPrecio = new TableColumn<>("Precio base");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioBase"));

        tabla.getColumns().add(colPrecio);
        tabla.setPlaceholder(new Label("No hay funciones programadas"));
        return tabla;
    }

    // Getters expuestos al controller
    public Button getBtnAgregar() { return btnAgregar; }
    public TableView<Funcion> getTabla() { return tabla; }
}