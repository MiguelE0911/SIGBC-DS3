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
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TableCell;

public class GestionUsuariosView extends VBox {

    private final TextField txtBuscar;
    private final ComboBox<String> cmbRol;
    private final TableView<UsuarioDTO> tablaUsuarios;
    private final Label lblUsuarioSeleccionado;
    private final Label lblEstadoSeleccionado;
    private final Label lblRolSeleccionado;
    private final ComboBox<String> cmbNuevoRol;
    private final Button btnGuardarCambios;
    private final Button btnSuspender;

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

        colEstado.setCellFactory(columna -> new TableCell<UsuarioDTO,
                Boolean>() {
            @Override
            protected void updateItem(Boolean activo, boolean empty) {
                super.updateItem(activo, empty);
                if (empty || activo == null) {
                    setText(null);
                } else {
                    setText(activo ? "Activo" : "Suspendido");
                }
            }
        });

        tablaUsuarios.getColumns().addAll(
                colNombre,
                colUsuario,
                colCorreo,
                colRol,
                colEstado
        );

        VBox.setVgrow(tablaUsuarios, Priority.ALWAYS);

        // Panel de edición
        VBox panelEdicion = new VBox(15);
        panelEdicion.setPadding(new Insets(20));
        panelEdicion.setPrefWidth(280);

        Label tituloPanel = new Label("Editar usuario");
        tituloPanel.getStyleClass().add("vista-titulo");

        Label lblUsuario = new Label("Usuario");
        lblUsuario.getStyleClass().add("vista-subtitulo");

        lblUsuarioSeleccionado = new Label("-");
        lblUsuarioSeleccionado.setStyle("-fx-text-fill: #f4e8d0;" +
                " -fx-font-size: 14px;");

        Label lblRol = new Label("Rol");
        lblRol.getStyleClass().add("vista-subtitulo");

        lblRolSeleccionado = new Label("-");
        lblRolSeleccionado.setStyle("-fx-text-fill: #f4e8d0;" +
                " -fx-font-size: 14px;");

        cmbNuevoRol = new ComboBox<>();
        cmbNuevoRol.getItems().addAll(
                "Administrador",
                "Personal",
                "Espectador"
        );
        cmbNuevoRol.setPrefWidth(220);
        cmbNuevoRol.getStyleClass().add("filtro-rol");

        // El ComboBox permanece oculto hasta seleccionar un usuario
        cmbNuevoRol.setVisible(false);
        cmbNuevoRol.setManaged(false);

        Label lblEstado = new Label("Estado");
        lblEstado.getStyleClass().add("vista-subtitulo");

        lblEstadoSeleccionado = new Label("-");
        lblEstadoSeleccionado.setStyle("-fx-text-fill: #f4e8d0; -fx-font-size: 14px;");

        btnGuardarCambios = new Button("Guardar cambios");
        btnGuardarCambios.getStyleClass().add("boton-principal");
        btnGuardarCambios.setMaxWidth(Double.MAX_VALUE);
        btnGuardarCambios.setDisable(true);

        btnSuspender = new Button("Suspender usuario");
        btnSuspender.getStyleClass().add("boton-secundario");
        btnSuspender.setMaxWidth(Double.MAX_VALUE);
        btnSuspender.setDisable(true);

        panelEdicion.getChildren().addAll(
                tituloPanel,
                lblUsuario,
                lblUsuarioSeleccionado,
                lblRol,
                lblRolSeleccionado,
                cmbNuevoRol,
                lblEstado,
                lblEstadoSeleccionado,
                btnGuardarCambios,
                btnSuspender
        );

        // Contenedor principal
        BorderPane contenido = new BorderPane();
        contenido.setCenter(tablaUsuarios);
        contenido.setRight(panelEdicion);

        BorderPane.setMargin(panelEdicion, new Insets(0, 0, 0, 25));

        VBox.setVgrow(contenido, Priority.ALWAYS);

        getChildren().addAll(
                titulo,
                subtitulo,
                barraFiltros,
                contenido
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

    public Label getLblUsuarioSeleccionado() {
        return lblUsuarioSeleccionado;
    }

    public Label getLblEstadoSeleccionado() {
        return lblEstadoSeleccionado;
    }

    public ComboBox<String> getCmbNuevoRol() {
        return cmbNuevoRol;
    }

    public Button getBtnGuardarCambios() {
        return btnGuardarCambios;
    }

    public Button getBtnSuspender() {
        return btnSuspender;
    }

    public Label getLblRolSeleccionado() {
        return lblRolSeleccionado;
    }
}