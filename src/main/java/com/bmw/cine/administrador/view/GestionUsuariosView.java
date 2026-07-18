package com.bmw.cine.administrador.view;

import com.bmw.cine.common.dto.UsuarioDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class GestionUsuariosView {

    // Contenedor principal
    private BorderPane root;

    // Título
    private Label lblTitulo;

    // Tabla de usuarios
    private TableView<UsuarioDTO> tblUsuarios;

    private TableColumn<UsuarioDTO, String> colNombre;
    private TableColumn<UsuarioDTO, String> colCorreo;
    private TableColumn<UsuarioDTO, String> colUsername;
    private TableColumn<UsuarioDTO, String> colRol;
    private TableColumn<UsuarioDTO, String> colEstado;

    // Botones
    private Button btnCambiarRol;
    private Button btnSuspender;
    private Button btnReactivar;

    /**
     * Constructor.
     */
    public GestionUsuariosView() {
        inicializarComponentes();
    }

    /**
     * Inicializa todos los componentes.
     */
    private void inicializarComponentes() {

        root = new BorderPane();
        root.setPadding(new Insets(20));

        crearTitulo();
        crearTabla();
        crearBotones();

    }

    /**
     * Crea el título de la vista.
     */
    private void crearTitulo() {

        lblTitulo = new Label("Gestión de Usuarios");
        lblTitulo.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #f4e8d0;"
        );

        BorderPane.setMargin(lblTitulo, new Insets(0, 0, 20, 0));
        root.setTop(lblTitulo);

    }

    /**
     * Crea la tabla de usuarios.
     */
    private void crearTabla() {

        tblUsuarios = new TableView<>();

        colNombre = new TableColumn<>("Nombre");
        colCorreo = new TableColumn<>("Correo");
        colUsername = new TableColumn<>("Username");
        colRol = new TableColumn<>("Rol");
        colEstado = new TableColumn<>("Estado");

        colNombre.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombre()));

        colCorreo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCorreo()));

        colUsername.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getUsername()));

        colRol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombreRol()));

        colEstado.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().isActivo() ? "Activo" : "Suspendido"));

        tblUsuarios.getColumns().addAll(
                colNombre,
                colCorreo,
                colUsername,
                colRol,
                colEstado
        );

        tblUsuarios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        root.setCenter(tblUsuarios);

    }

    /**
     * Crea los botones inferiores.
     */
    private void crearBotones() {

        btnCambiarRol = new Button("Cambiar Rol");
        btnSuspender = new Button("Suspender");
        btnReactivar = new Button("Reactivar");

        String estiloBoton =
                "-fx-background-color: #d4af37;" +
                        "-fx-text-fill: #1b1224;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;";

        btnCambiarRol.setStyle(estiloBoton);
        btnSuspender.setStyle(estiloBoton);
        btnReactivar.setStyle(estiloBoton);

        HBox botones = new HBox(15);
        botones.setAlignment(Pos.CENTER_RIGHT);
        botones.setPadding(new Insets(20, 0, 0, 0));

        botones.getChildren().addAll(
                btnCambiarRol,
                btnSuspender,
                btnReactivar
        );

        root.setBottom(botones);

    }

    // Getters
    public BorderPane getRoot() {
        return root;
    }

    public TableView<UsuarioDTO> getTblUsuarios() {
        return tblUsuarios;
    }

    public Button getBtnCambiarRol() {
        return btnCambiarRol;
    }

    public Button getBtnSuspender() {
        return btnSuspender;
    }

    public Button getBtnReactivar() {
        return btnReactivar;
    }

}
