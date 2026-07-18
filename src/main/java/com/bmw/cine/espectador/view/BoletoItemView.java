package com.bmw.cine.espectador.view;

import com.bmw.cine.espectador.model.Boleto;
import com.bmw.cine.espectador.model.QrGenerator; // Importación necesaria para la utilidad

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Componente visual que representa una fila individual en la Billetera.
 * Muestra el QR del boleto generado dinámicamente, detalles de la función y el estado.
 * 
 * @author Wilma
 * @version 1.1
 */
public class BoletoItemView extends HBox {

    private final Boleto boleto;

    public BoletoItemView(Boleto boleto) {
        this.boleto = boleto;
        configurarEstiloFila();
        inicializarComponentes();
    }

    private void configurarEstiloFila() {
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(15);
        this.setPadding(new Insets(15));
        
        // Estilo de tarjeta basado en tu identidad visual VIP [2, 3]
        this.setStyle(
            "-fx-background-color: #241a30; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #3a2b4a; " +
            "-fx-border-width: 1; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 4);"
        );
    }

    private void inicializarComponentes() {
        // 1. Representación del QR Funcional
        // Preparamos la información que contendrá el QR según los requisitos [1]
        String infoQr = String.format("ID Compra: %d | Película: %s | Asiento: %s", 
                                      boleto.getIdCompra(), 
                                      boleto.getPelicula(), 
                                      boleto.getAsiento());

        ImageView imgQR = new ImageView();
        imgQR.setFitWidth(70);
        imgQR.setFitHeight(70);
        
        // Llamada a la utilidad QrGenerator para obtener la imagen real [4]
        imgQR.setImage(QrGenerator.generarImagenQr(infoQr)); 

        // 2. Información del Boleto (Columna Central)
        VBox infoContainer = new VBox(5);
        infoContainer.setAlignment(Pos.CENTER_LEFT);

        Label lblPelicula = new Label(boleto.getPelicula().toUpperCase());
        lblPelicula.setStyle("-fx-font-weight: bold; -fx-text-fill: #f4e8d0; -fx-font-size: 14px;");

        Label lblDetalle = new Label("Asiento: " + boleto.getAsiento() + " | " + boleto.getFecha().toLocalDate());
        lblDetalle.setStyle("-fx-text-fill: #b8a9c9; -fx-font-size: 11px;");

        infoContainer.getChildren().addAll(lblPelicula, lblDetalle);

        // 3. Indicador de Estado (Derecha)
        Label lblEstado = new Label(boleto.getEstado().toString());
        lblEstado.setPadding(new Insets(4, 10, 4, 10));
        lblEstado.setStyle(obtenerEstiloEstado(boleto.getEstado()));

        // Asegurar que el estado se empuje a la derecha para un diseño balanceado [5]
        HBox.setHgrow(infoContainer, javafx.scene.layout.Priority.ALWAYS);

        this.getChildren().addAll(imgQR, infoContainer, lblEstado);
    }

    /**
     * Devuelve el estilo CSS según el estado del boleto.
     * Verde para CONFIRMADO, Naranja para PENDIENTE [6].
     */
    private String obtenerEstiloEstado(Boleto.EstadoBoleto estado) {
        String baseStyle = "-fx-background-radius: 15; -fx-font-weight: bold; -fx-font-size: 10px; ";
        
        if (estado == Boleto.EstadoBoleto.CONFIRMADO) {
            // Verde esmeralda para éxito
            return baseStyle + "-fx-background-color: rgba(46, 204, 113, 0.2); -fx-text-fill: #2ecc71; -fx-border-color: #2ecc71; -fx-border-width: 1;";
        } else {
            // Naranja vibrante para pendiente
            return baseStyle + "-fx-background-color: rgba(230, 126, 34, 0.2); -fx-text-fill: #e67e22; -fx-border-color: #e67e22; -fx-border-width: 1;";
        }
    }
}