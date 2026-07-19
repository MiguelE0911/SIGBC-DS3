package com.bmw.cine.administrador.controller;

import com.bmw.cine.administrador.view.GestionUsuariosView;
import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dto.UsuarioDTO;

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

    public GestionUsuariosController(GestionUsuariosView vista, UsuarioDAO usuarioDAO) {

        this.vista = vista;
        this.usuarioDAO = usuarioDAO;

        cargarUsuarios();
        configurarFiltros();
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

    /**
     * Permite volver a cargar la tabla cuando haya cambios.
     */
    public void recargarTabla() {
        cargarUsuarios();
        aplicarFiltros();
    }
}