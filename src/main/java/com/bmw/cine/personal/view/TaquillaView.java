package com.bmw.cine.personal.view;

import com.bmw.cine.common.dto.UsuarioDTO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Cuerpo de la sección Taquilla dentro del Panel de Personal.
 * Versión autónoma con estilos inline inmunes a fallos de CSS externo.
 */
public class TaquillaView extends VBox {

    public TaquillaView(UsuarioDTO usuarioActivo) {

        this.setStyle("-fx-background-color: #100b16;");

        this.setAlignment(Pos.CENTER);
        this.setSpacing(14);
        this.setPadding(new Insets(48));

        Label titulo = new Label("Taquilla");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f4e8d0; -fx-font-family: 'Segoe UI';");

        Label subtitulo = new Label("Bandeja de solicitudes y emisión de boletos — próximamente");
        subtitulo.setStyle("-fx-font-size: 13px; -fx-text-fill: #b8a9c9; -fx-font-family: 'Segoe UI';");
        subtitulo.setWrapText(true);

        Label sesion = new Label("Atendiendo como: " + usuarioActivo.getNombre());
        sesion.setStyle("-fx-font-size: 13px; -fx-text-fill: #b8a9c9; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold;");

        getChildren().addAll(titulo, subtitulo, sesion);
    }
}