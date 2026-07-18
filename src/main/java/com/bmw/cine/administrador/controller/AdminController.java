package com.bmw.cine.administrador.controller;

import com.bmw.cine.administrador.view.AdminView;
import com.bmw.cine.administrador.view.GestionUsuariosView;
import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dao.impl.UsuarioDAOImpl;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.dao.DAOException;
import javafx.scene.control.Alert;
import java.util.List;
import javafx.stage.Stage;

public class AdminController {

    private final AdminView vista;
    private final Stage stage;
    private final GestionUsuariosView gestionUsuariosView;
    private final UsuarioDAO usuarioDAO;

    public AdminController(AdminView vista, Stage stage) {
        this.vista = vista;
        this.stage = stage;

        this.gestionUsuariosView = new GestionUsuariosView();
        this.usuarioDAO = new UsuarioDAOImpl();

        configurarEventos();
    }


    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText("Operación no completada");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void cargarUsuarios() {
        try {
            List<UsuarioDTO> usuarios = usuarioDAO.listarTodos();
            gestionUsuariosView.getTblUsuarios().getItems().setAll(usuarios);
        } catch (DAOException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Configura todos los eventos de la vista.
     */
    private void configurarEventos() {

        vista.getBtnGestionUsuarios().setOnAction(e -> {
            cargarUsuarios();
            vista.getContentPane().getChildren().setAll(
                    gestionUsuariosView.getRoot()
            );

        });

        vista.getBtnReportes().setOnAction(e -> {
            System.out.println("Reportes");
        });

        vista.getItemMiInformacion().setOnAction(e -> {
            System.out.println("Mi informacion");
        });

        vista.getItemCambiarSeccion().setOnAction(e -> {
            System.out.println("Cambiar de seccion");
        });

        vista.getItemCerrarSesion().setOnAction(e -> {
            System.out.println("Cerrar sesion");
        });

    }

}
