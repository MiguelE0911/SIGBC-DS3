package com.bmw.cine.administrador.controller;

import com.bmw.cine.administrador.view.GestionUsuariosView;
import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dto.UsuarioDTO;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.List;

public class GestionUsuariosController {

    private final GestionUsuariosView vista;
    private final UsuarioDAO usuarioDAO;

    public GestionUsuariosController(GestionUsuariosView vista, UsuarioDAO usuarioDAO) {
        this.vista = vista;
        this.usuarioDAO = usuarioDAO;

        cargarUsuarios();
    }

    /**
     * Obtiene todos los usuarios desde la base de datos
     * y los muestra en la tabla.
     */
    private void cargarUsuarios() {

        try {

            List<UsuarioDTO> usuarios = usuarioDAO.listarTodos();

            vista.getTablaUsuarios().setItems(
                    FXCollections.observableArrayList(usuarios)
            );

        } catch (DAOException e) {

            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("No fue posible cargar los usuarios.");
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }
    }

    /**
     * Permite volver a cargar la tabla cuando sea necesario
     * (por ejemplo después de suspender un usuario o cambiarle el rol).
     */
    public void recargarTabla() {
        cargarUsuarios();
    }
}