package com.bmw.cine.personal.view;

import com.bmw.cine.common.dto.FuncionDTO;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Pelicula;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Vista de Emisión manual de boletos — solo construcción de componentes
 * visuales. Toda la lógica (búsqueda de usuario, carga de asientos,
 * emisión) vive en EmitirBoletoController.
 *
 * NOTA: la selección de asiento usa un ListView de códigos generados a
 * partir de filas/columnas de la sala (A1, A2, ...) como reemplazo
 * temporal del mapa de asientos visual del Espectador. Cuando ese mapa
 * exista, esta pantalla debería reutilizarlo en vez de este ListView.
 */
public class EmitirBoletoView extends BorderPane {

    //  Búsqueda de usuario
    private final TextField campoBusquedaUsuario = new TextField();
    private final Button btnBuscar = new Button("Buscar");
    private final ListView<UsuarioDTO> listaUsuarios = new ListView<>();
    private final Label labelUsuarioSeleccionado = new Label("Ningún usuario seleccionado");

    // Película / función
    private final ComboBox<Pelicula> comboPelicula = new ComboBox<>();
    private final ComboBox<FuncionDTO> comboFuncion = new ComboBox<>();

    // --- Asientos (placeholder del mapa real) ---
    private final ListView<String> listaAsientos = new ListView<>();
    private final Label labelAsientosInfo = new Label();

    private final Button btnEmitir = new Button("Emitir boleto(s)");

    public EmitirBoletoView() {
        getStyleClass().add("vista-cuerpo");
        setPadding(new Insets(24));

        setTop(construirEncabezado());
        setCenter(construirFormulario());
    }

    private VBox construirEncabezado() {
        Label titulo = new Label("Emisión manual de boletos");
        titulo.getStyleClass().add("vista-titulo");
        VBox encabezado = new VBox(8, titulo);
        encabezado.setPadding(new Insets(0, 0, 16, 0));
        return encabezado;
    }

    private VBox construirFormulario() {
        // Sección usuario
        Label lblSeccionUsuario = new Label("1. Espectador");
        lblSeccionUsuario.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: -fx-acento-principal;");

        campoBusquedaUsuario.setPromptText("Buscar por nombre, correo o username...");
        btnBuscar.getStyleClass().add("boton-secundario");
        HBox filaBusqueda = new HBox(8, campoBusquedaUsuario, btnBuscar);
        filaBusqueda.setAlignment(Pos.CENTER_LEFT);

        listaUsuarios.setPrefHeight(120);
        listaUsuarios.setPlaceholder(new Label("Buscá un usuario para ver resultados"));

        labelUsuarioSeleccionado.getStyleClass().add("vista-subtitulo");

        VBox seccionUsuario = new VBox(8,
                lblSeccionUsuario, filaBusqueda, listaUsuarios, labelUsuarioSeleccionado);

        // Sección película/función
        Label lblSeccionFuncion = new Label("2. Película y función");
        lblSeccionFuncion.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: -fx-acento-principal;");

        comboPelicula.setPromptText("Seleccionar película");
        comboFuncion.setPromptText("Seleccionar función");
        HBox filaPeliculaFuncion = new HBox(12, comboPelicula, comboFuncion);
        filaPeliculaFuncion.setAlignment(Pos.CENTER_LEFT);

        VBox seccionFuncion = new VBox(8,
                lblSeccionFuncion, filaPeliculaFuncion);

        // Sección asientos
        Label lblSeccionAsientos = new Label("3. Asiento(s) — selección múltiple");
        lblSeccionAsientos.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: -fx-acento-principal;");

        listaAsientos.setPrefHeight(160);
        listaAsientos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listaAsientos.setPlaceholder(new Label("Elegí una función primero"));
        labelAsientosInfo.getStyleClass().add("vista-subtitulo");

        VBox seccionAsientos = new VBox(8,
                lblSeccionAsientos, listaAsientos, labelAsientosInfo);

        // Botón emitir
        btnEmitir.getStyleClass().add("boton-aprobar");

        VBox contenedor = new VBox(24, seccionUsuario, new Separator(), seccionFuncion,
                new Separator(), seccionAsientos, btnEmitir);
        contenedor.setMaxWidth(520);
        return contenedor;
    }

    // Getters expuestos al controller
    public TextField getCampoBusquedaUsuario() { return campoBusquedaUsuario; }
    public Button getBtnBuscar() { return btnBuscar; }
    public ListView<UsuarioDTO> getListaUsuarios() { return listaUsuarios; }
    public Label getLabelUsuarioSeleccionado() { return labelUsuarioSeleccionado; }
    public ComboBox<Pelicula> getComboPelicula() { return comboPelicula; }
    public ComboBox<FuncionDTO> getComboFuncion() { return comboFuncion; }
    public ListView<String> getListaAsientos() { return listaAsientos; }
    public Label getLabelAsientosInfo() { return labelAsientosInfo; }
    public Button getBtnEmitir() { return btnEmitir; }
}