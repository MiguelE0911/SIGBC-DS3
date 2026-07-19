package com.bmw.cine.espectador.view;

import com.bmw.cine.common.dto.PeliculaCardDTO;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class PeliculaCardView extends VBox {
    private final PeliculaCardDTO pelicula;

    public PeliculaCardView(PeliculaCardDTO pelicula) {
        this.pelicula = pelicula;
        configurarEstilos();
        inicializarComponentes();
    }

    private void configurarEstilos() {
        this.setSpacing(10);
        this.setPadding(new Insets(15));
        this.setAlignment(Pos.TOP_CENTER);
        this.setPrefWidth(200);
        this.setStyle(
            "-fx-background-color: #241a30; " + // -fx-fondo-tarjetas
            "-fx-background-radius: 14; " +
            "-fx-border-color: #3a2b4a; " +     // -fx-borde-sutil
            "-fx-border-radius: 14; " +
            "-fx-cursor: hand;"
        );

        // Efecto hover dorado [3]
        this.setOnMouseEntered(e -> this.setStyle(this.getStyle() + "-fx-border-color: #d4af37; -fx-effect: dropshadow(gaussian, rgba(212,175,55,0.3), 10, 0, 0, 0);"));
        this.setOnMouseExited(e -> this.setStyle(this.getStyle().replace("-fx-border-color: #d4af37;", "-fx-border-color: #3a2b4a;")));
    }

    private void inicializarComponentes() {
        // Póster [5]
        ImageView imgPoster = new ImageView();
        try {
            imgPoster.setImage(new Image(pelicula.getRutaPoster(), true));
        } catch (Exception e) {
            // Imagen por defecto si falla la carga
        }
        imgPoster.setFitWidth(170);
        imgPoster.setFitHeight(250);
        imgPoster.setPreserveRatio(false);

        // Título [5]
        Label lblTitulo = new Label(pelicula.getTitulo().toUpperCase());
        lblTitulo.setStyle("-fx-text-fill: #f4e8d0; -fx-font-weight: bold; -fx-font-size: 14px;");
        lblTitulo.setWrapText(true);

        // Género y Duración [5]
        Label lblInfo = new Label(pelicula.getGenero() + " • " + pelicula.getDuracionMinutos() + " min");
        lblInfo.setStyle("-fx-text-fill: #b8a9c9; -fx-font-size: 11px;");

        this.getChildren().addAll(imgPoster, lblTitulo, lblInfo);
    }

    public PeliculaCardDTO getPelicula() { return pelicula; }
}