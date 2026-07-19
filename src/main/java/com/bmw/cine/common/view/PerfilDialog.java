package com.bmw.cine.common.view;

import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dto.UsuarioDTO;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Window;

import java.util.Optional;

/**
 * Diálogo de "Mi perfil", compartido por los 3 flujos (Personal,
 * Administrador, Espectador). Edita nombre/correo/username; NO toca
 * password_hash, rol_id ni activo — eso queda fuera del alcance de esta
 * pantalla.
 *
 * Uso:
 *   PerfilDialog.mostrar(stage, usuarioActivo, usuarioDAO)
 *       .ifPresent(actualizado -> headerCtrl.actualizarNombreUsuario(actualizado));
 */
public class PerfilDialog {

    public static Optional<UsuarioDTO> mostrar(Window owner, UsuarioDTO usuarioActual, UsuarioDAO usuarioDAO) {
        Dialog<UsuarioDTO> dialog = new Dialog<>();
        dialog.setTitle("Mi perfil");
        if (owner != null) {
            dialog.initOwner(owner);
        }

        dialog.setResizable(true);
        dialog.getDialogPane().setMinWidth(380);
        dialog.getDialogPane().setMinHeight(220);
        dialog.getDialogPane().setMaxWidth(700);
        dialog.getDialogPane().setMaxHeight(600);
        dialog.getDialogPane().setPrefWidth(420);

        ButtonType btnGuardarTipo = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardarTipo, ButtonType.CANCEL);

        TextField txtNombre = new TextField(usuarioActual.getNombre());
        TextField txtCorreo = new TextField(usuarioActual.getCorreo());
        TextField txtUsername = new TextField(usuarioActual.getUsername());

        // Que los campos crezcan horizontalmente junto con el diálogo
        txtNombre.setMaxWidth(Double.MAX_VALUE);
        txtCorreo.setMaxWidth(Double.MAX_VALUE);
        txtUsername.setMaxWidth(Double.MAX_VALUE);

        Label lblRol = new Label(usuarioActual.getNombreRol());
        lblRol.setStyle("-fx-text-fill: -fx-texto-subtitulos;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(new Insets(16, 8, 8, 0));

        // Columna 0 (labels) fija; columna 1 (campos) crece con el diálogo
        ColumnConstraints colLabel = new ColumnConstraints();
        colLabel.setMinWidth(80);
        ColumnConstraints colCampo = new ColumnConstraints();
        colCampo.setHgrow(Priority.ALWAYS);
        colCampo.setFillWidth(true);
        grid.getColumnConstraints().addAll(colLabel, colCampo);

        grid.addRow(0, new Label("Nombre:"), txtNombre);
        grid.addRow(1, new Label("Correo:"), txtCorreo);
        grid.addRow(2, new Label("Username:"), txtUsername);
        grid.addRow(3, new Label("Rol:"), lblRol);

        dialog.getDialogPane().setContent(grid);

        Node botonGuardar = dialog.getDialogPane().lookupButton(btnGuardarTipo);
        botonGuardar.addEventFilter(ActionEvent.ACTION, event -> {
            String error = validar(txtNombre, txtCorreo, txtUsername, usuarioActual, usuarioDAO);
            if (error != null) {
                mostrarAlerta("Datos inválidos", error, Alert.AlertType.WARNING);
                event.consume();
            }
        });

        dialog.setResultConverter(boton -> {
            if (boton != btnGuardarTipo) return null;

            try {
                boolean actualizado = usuarioDAO.actualizarPerfil(
                        usuarioActual.getId(),
                        txtNombre.getText().trim(),
                        txtCorreo.getText().trim(),
                        txtUsername.getText().trim());

                if (!actualizado) {
                    mostrarAlerta("Error", "No se pudo actualizar el perfil.", Alert.AlertType.ERROR);
                    return null;
                }

                return new UsuarioDTO(usuarioActual.getId(), txtNombre.getText().trim(),
                        txtCorreo.getText().trim(), txtUsername.getText().trim(),
                        usuarioActual.getRol(), usuarioActual.isActivo());
            } catch (DAOException e) {
                mostrarAlerta("Error", "No se pudo actualizar el perfil: " + e.getMessage(), Alert.AlertType.ERROR);
                return null;
            }
        });

        return dialog.showAndWait();
    }

    private static String validar(TextField txtNombre, TextField txtCorreo, TextField txtUsername,
                                  UsuarioDTO usuarioActual, UsuarioDAO usuarioDAO) {
        if (txtNombre.getText().isBlank()) return "El nombre es obligatorio.";
        if (txtCorreo.getText().isBlank()) return "El correo es obligatorio.";
        if (txtUsername.getText().isBlank()) return "El username es obligatorio.";

        String correo = txtCorreo.getText().trim();
        String username = txtUsername.getText().trim();

        if (!correo.equals(usuarioActual.getCorreo())
                && usuarioDAO.existeCorreoExcluyendo(correo, usuarioActual.getId())) {
            return "Ya existe otra cuenta con ese correo.";
        }
        if (!username.equals(usuarioActual.getUsername())
                && usuarioDAO.existeUsernameExcluyendo(username, usuarioActual.getId())) {
            return "Ya existe otra cuenta con ese username.";
        }
        return null;
    }

    private static void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}