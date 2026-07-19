package com.bmw.cine.administrador.controller;

import com.bmw.cine.administrador.view.GestionUsuariosView;
import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Usuario;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.List;
import java.util.stream.Collectors;

public class GestionUsuariosController {

    private final GestionUsuariosView vista;
    private final UsuarioDAO usuarioDAO;

    // Lista completa de usuarios obtenida desde la BD
    private List<UsuarioDTO> usuarios;

    private UsuarioDTO usuarioSeleccionado;

    public GestionUsuariosController(GestionUsuariosView vista, UsuarioDAO usuarioDAO) {

        this.vista = vista;
        this.usuarioDAO = usuarioDAO;

        cargarUsuarios();
        configurarFiltros();
        configurarSeleccionTabla();
        configurarBotones();
    }

    private void configurarSeleccionTabla() {
        vista.getTablaUsuarios()
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, anterior, usuario) -> {

                    usuarioSeleccionado = usuario;
                    if (usuario == null) {
                        return;
                    }

                    vista.getLblUsuarioSeleccionado()
                            .setText(usuario.getUsername());
                    vista.getLblEstadoSeleccionado()
                            .setText(usuario.isActivo()
                                    ? "Activo"
                                    : "Suspendido");

                    // Ocultar el "-"
                    vista.getLblRolSeleccionado().setVisible(false);
                    vista.getLblRolSeleccionado().setManaged(false);

                    // Mostrar el ComboBox
                    vista.getCmbNuevoRol().setVisible(true);
                    vista.getCmbNuevoRol().setManaged(true);

                    vista.getCmbNuevoRol()
                            .setValue(usuario.getNombreRol());

                    vista.getBtnGuardarCambios()
                            .setDisable(true);

                    vista.getBtnSuspender()
                            .setDisable(false);
                });
    }



    private void configurarBotones() {
        vista.getBtnGuardarCambios().setOnAction(e -> {

            if (usuarioSeleccionado == null) {
                return;
            }

            String nuevoRol = vista.getCmbNuevoRol().getValue();

            int rolBD = convertirRol(nuevoRol);

            try {

                boolean actualizado = usuarioDAO.actualizarRol(
                        usuarioSeleccionado.getId(),
                        rolBD
                );

                if (actualizado) {

                    Alert alerta = new Alert(AlertType.INFORMATION);
                    alerta.setHeaderText(null);
                    alerta.setContentText("Rol actualizado correctamente.");
                    alerta.showAndWait();

                    recargarTabla();

                    vista.getTablaUsuarios()
                            .getSelectionModel()
                            .select(usuarioSeleccionado);

                } else {

                    Alert alerta = new Alert(AlertType.ERROR);
                    alerta.setHeaderText(null);
                    alerta.setContentText("No fue posible actualizar el rol.");
                    alerta.showAndWait();
                }

            } catch (DAOException ex) {

                Alert alerta = new Alert(AlertType.ERROR);
                alerta.setHeaderText(null);
                alerta.setContentText(ex.getMessage());
                alerta.showAndWait();
            }
        });

        vista.getBtnSuspender().setOnAction(e -> {
            if (usuarioSeleccionado == null) {
                return;
            }

            Alert alerta = new Alert(AlertType.INFORMATION);
            alerta.setHeaderText(null);
            alerta.setContentText(
                    "Aquí se suspenderá el usuario."
            );
            alerta.showAndWait();
        });
    }

    /**
     * Carga todos los usuarios desde la base de datos.
     */
    private void cargarUsuarios() {

        try {

            usuarios = usuarioDAO.listarTodos();

            vista.getTablaUsuarios().setItems(
                    FXCollections.observableArrayList(usuarios));

        } catch (DAOException e) {

            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("No fue posible cargar los usuarios.");
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }
    }

    /**
     * Configura los eventos de búsqueda y filtro.
     */
    private void configurarFiltros() {

        vista.getTxtBuscar().textProperty().addListener(
                (obs, anterior, nuevo) -> aplicarFiltros());

        vista.getCmbRol().valueProperty().addListener(
                (obs, anterior, nuevo) -> aplicarFiltros());
        // Habilitar el botón solo cuando el rol cambie
        vista.getCmbNuevoRol().valueProperty().addListener((obs, anterior, nuevo) -> {
            UsuarioDTO usuario = vista.getTablaUsuarios()
                    .getSelectionModel()
                    .getSelectedItem();
            if (usuario == null) {
                vista.getBtnGuardarCambios().setDisable(true);
                return;
            }
            boolean huboCambio =
                    !nuevo.equalsIgnoreCase(usuario.getNombreRol());
            vista.getBtnGuardarCambios().setDisable(!huboCambio);
        });
    }

    /**
     * Aplica los filtros por nombre, usuario y rol.
     */
    private void aplicarFiltros() {

        String textoBusqueda = vista.getTxtBuscar()
                .getText()
                .trim()
                .toLowerCase();

        String rolSeleccionado = vista.getCmbRol().getValue();

        List<UsuarioDTO> resultado = usuarios.stream()

                .filter(usuario -> {

                    boolean coincideTexto =
                            usuario.getNombre().toLowerCase().contains(textoBusqueda)
                                    || usuario.getUsername().toLowerCase().contains(textoBusqueda);

                    boolean coincideRol =
                            rolSeleccionado.equals("Todos")
                                    || usuario.getNombreRol().equalsIgnoreCase(rolSeleccionado);

                    return coincideTexto && coincideRol;
                })

                .collect(Collectors.toList());

        vista.getTablaUsuarios().setItems(
                FXCollections.observableArrayList(resultado));
    }

    private int convertirRol(String rol) {
        switch (rol) {
            case "Administrador":
                return Usuario.ROL_ADMINISTRADOR;
            case "Personal":
                return Usuario.ROL_PERSONAL;
            default:
                return Usuario.ROL_ESPECTADOR;
        }
    }

    /**
     * Permite volver a cargar la tabla cuando haya cambios.
     */
    public void recargarTabla() {
        cargarUsuarios();
        aplicarFiltros();
    }
}