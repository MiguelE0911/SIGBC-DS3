package com.bmw.cine.administrador.view;

import com.bmw.cine.common.dto.UsuarioDTO;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;

public class GestionUsuariosView extends VBox {

    private final TableView<UsuarioDTO> tablaUsuarios;

    public GestionUsuariosView() {

        setSpacing(20);
        setPadding(new Insets(20));
        getStyleClass().add("vista-cuerpo");

        Label titulo = new Label("Gestión de Usuarios");
        titulo.getStyleClass().add("vista-titulo");

        Label subtitulo = new Label("Listado de todas las cuentas registradas.");
        subtitulo.getStyleClass().add("vista-subtitulo");

        tablaUsuarios = new TableView<>();

        tablaUsuarios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Nombre
        TableColumn<UsuarioDTO, String> colNombre =
                new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre"));

        // Usuario
        TableColumn<UsuarioDTO, String> colUsuario =
                new TableColumn<>("Usuario");
        colUsuario.setCellValueFactory(
                new PropertyValueFactory<>("username"));

        // Correo
        TableColumn<UsuarioDTO, String> colCorreo =
                new TableColumn<>("Correo");
        colCorreo.setCellValueFactory(
                new PropertyValueFactory<>("correo"));

        // Rol
        TableColumn<UsuarioDTO, String> colRol =
                new TableColumn<>("Rol");
        colRol.setCellValueFactory(
                new PropertyValueFactory<>("nombreRol"));

        // Estado
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
                tablaUsuarios
        );
    }

    public TableView<UsuarioDTO> getTablaUsuarios() {
        return tablaUsuarios;
    }
}