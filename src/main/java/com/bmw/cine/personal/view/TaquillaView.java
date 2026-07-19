package com.bmw.cine.personal.view;

import com.bmw.cine.common.dto.FuncionDTO;
import com.bmw.cine.common.dto.SolicitudBoletoDTO;
import com.bmw.cine.common.model.Pelicula;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TaquillaView extends BorderPane {

    private final ComboBox<Pelicula> comboPelicula = new ComboBox<>();
    private final ComboBox<FuncionDTO> comboFuncion = new ComboBox<>();
    private final ComboBox<String> comboEstado = new ComboBox<>();
    private final Button btnActualizar = new Button("Actualizar");
    private final TableView<SolicitudBoletoDTO> tabla = new TableView<>();

    public TaquillaView() {
        getStyleClass().add("vista-cuerpo");
        setPadding(new Insets(24));

        setTop(construirEncabezado());
        setCenter(construirTabla());
    }

    private VBox construirEncabezado() {
        Label titulo = new Label("Bandeja de solicitudes");
        titulo.getStyleClass().add("vista-titulo");

        comboPelicula.setPromptText("Todas las películas");
        comboFuncion.setPromptText("Todas las funciones");
        comboEstado.setPromptText("Estado");
        btnActualizar.getStyleClass().add("boton-secundario");

        HBox filtros = new HBox(12, comboPelicula, comboFuncion, comboEstado, btnActualizar);
        filtros.setAlignment(Pos.CENTER_LEFT);
        filtros.setPadding(new Insets(16, 0, 16, 0));

        return new VBox(8, titulo, filtros);
    }

    private TableView<SolicitudBoletoDTO> construirTabla() {
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<SolicitudBoletoDTO, String> colUsuario = new TableColumn<>("Usuario");
        colUsuario.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNombreUsuario()));

        TableColumn<SolicitudBoletoDTO, String> colPelicula = new TableColumn<>("Película");
        colPelicula.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getTituloPelicula()));

        TableColumn<SolicitudBoletoDTO, String> colFuncion = new TableColumn<>("Función");
        colFuncion.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getHorarioFuncion().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

        TableColumn<SolicitudBoletoDTO, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getEstado()));

        tabla.getColumns().addAll(colUsuario, colPelicula, colFuncion, colEstado);
        tabla.setPlaceholder(new Label("No hay solicitudes con estos filtros"));

        return tabla;
    }

    // Getters expuestos al controller
    public ComboBox<Pelicula> getComboPelicula() { return comboPelicula; }
    public ComboBox<FuncionDTO> getComboFuncion() { return comboFuncion; }
    public ComboBox<String> getComboEstado() { return comboEstado; }
    public Button getBtnActualizar() { return btnActualizar; }
    public TableView<SolicitudBoletoDTO> getTabla() { return tabla; }
}