package com.bmw.cine.administrador.view;

import com.bmw.cine.common.dto.UsuarioDTO;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GestionUsuariosView extends VBox {

    private final TextField txtBuscar;
    private final ComboBox<String> cmbRol;
    private final TableView<UsuarioDTO> tablaUsuarios;

    public GestionUsuariosView() {

        setSpacing(20);
        setPadding(new Insets(20));
        getStyleClass().add("vista-cuerpo");

        // Título
        Label titulo = new Label("Gestión de Usuarios");
        titulo.getStyleClass().add("vista-titulo");

        // Subtítulo
        Label subtitulo = new Label("Listado de todas las cuentas registradas.");
        subtitulo.getStyleClass().add("vista-subtitulo");

        // Barra de filtros
        HBox barraFiltros = new HBox(15);

        txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar por nombre o usuario...");
        txtBuscar.setPrefWidth(350);
        txtBuscar.getStyleClass().add("busqueda");

        cmbRol = new ComboBox<>();
        cmbRol.getItems().addAll(
                "Todos",
                "Administrador",
                "Personal",
                "Espectador"
        );
        cmbRol.setValue("Todos");
        cmbRol.setPrefWidth(180);
        cmbRol.getStyleClass().add("filtro-rol");

        barraFiltros.getChildren().addAll(txtBuscar, cmbRol);

        // Tabla
        tablaUsuarios = new TableView<>();
        tablaUsuarios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaUsuarios.getStyleClass().add("tabla-usuarios");

        TableColumn<UsuarioDTO, String> colNombre =
                new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre"));

        TableColumn<UsuarioDTO, String> colUsuario =
                new TableColumn<>("Usuario");
        colUsuario.setCellValueFactory(
                new PropertyValueFactory<>("username"));

        TableColumn<UsuarioDTO, String> colCorreo =
                new TableColumn<>("Correo");
        colCorreo.setCellValueFactory(
                new PropertyValueFactory<>("correo"));

        TableColumn<UsuarioDTO, String> colRol =
                new TableColumn<>("Rol");
        colRol.setCellValueFactory(
                new PropertyValueFactory<>("nombreRol"));

        TableColumn<UsuarioDTO, Boolean> colEstado =
                new TableColumn<>("Estado");
        colEstado.setCellValueFactory(
                new PropertyValueFactory<>("activo"));

        tablaUsuarios.getColumns().addAll(
                colNombre,
                colUsuario,
                colCorreo,
                colRol,
                colEstado
        );

        VBox.setVgrow(tablaUsuarios, Priority.ALWAYS);

        getChildren().addAll(
                titulo,
                subtitulo,
                barraFiltros,
                tablaUsuarios
        );
    }

    public TextField getTxtBuscar() {
        return txtBuscar;
    }

    public ComboBox<String> getCmbRol() {
        return cmbRol;
    }

    public TableView<UsuarioDTO> getTablaUsuarios() {
        return tablaUsuarios;
    }
}