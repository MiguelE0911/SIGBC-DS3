package com.bmw.cine.espectador.view;

import java.io.File;
import java.io.IOException;

import com.bmw.cine.espectador.model.Boleto;
import com.bmw.cine.espectador.model.BoletoPdfExporter;
import com.bmw.cine.espectador.model.QrGenerator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * Componente visual que representa una fila individual en la Billetera.
 * Muestra el QR del boleto generado dinámicamente, detalles de la función,
 * el estado, y permite exportar el boleto como PDF.
 *
 * @author Wilma
 * @version 1.2
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

        this.setStyle(
            "-fx-background-color: #241a30; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #3a2b4a; " +
            "-fx-border-width: 1; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 4);"
        );
    }

    private void inicializarComponentes() {
        String infoQr = String.format(
            "🎟 MULTICINES BMW 🎟\n" +
            "--------------------\n" +
            "ORDEN: #%d\n" +
            "PELÍCULA: %s\n" +
            "ASIENTO: %s\n" +
            "ESTADO: %s",
            boleto.getIdCompra(),
            boleto.getPelicula().toUpperCase(),
            boleto.getAsiento(),
            boleto.getEstadoTexto()
        );

        ImageView imgQR = new ImageView();
        imgQR.setFitWidth(100);
        imgQR.setFitHeight(100);
        imgQR.setImage(QrGenerator.generarImagenQr(infoQr));

        VBox infoContainer = new VBox(5);
        infoContainer.setAlignment(Pos.CENTER_LEFT);
        Label lblPelicula = new Label(boleto.getPelicula().toUpperCase());
        lblPelicula.setStyle("-fx-font-weight: bold; -fx-text-fill: #f4e8d0; -fx-font-size: 14px;");
        Label lblDetalle = new Label("Asiento: " + boleto.getAsiento() + " | " + boleto.getFecha().toLocalDate());
        lblDetalle.setStyle("-fx-text-fill: #b8a9c9; -fx-font-size: 11px;");
        infoContainer.getChildren().addAll(lblPelicula, lblDetalle);

        Label lblEstado = new Label(boleto.getEstadoTexto());
        lblEstado.setPadding(new Insets(4, 10, 4, 10));
        lblEstado.setStyle(obtenerEstiloEstado(boleto.getEstado()));

        Button btnExportar = new Button("📄 PDF");
        btnExportar.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: #d4af37; -fx-border-color: #d4af37; " +
            "-fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 4 10 4 10; " +
            "-fx-font-size: 10px; -fx-cursor: hand;"
        );
        btnExportar.setOnAction(e -> exportarPdf());

        HBox.setHgrow(infoContainer, javafx.scene.layout.Priority.ALWAYS);
        this.getChildren().addAll(imgQR, infoContainer, lblEstado, btnExportar);
    }

    private void exportarPdf() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Guardar boleto como PDF");
        chooser.setInitialFileName("Boleto_" + boleto.getIdCompra() + "_" + boleto.getAsiento() + ".pdf");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf"));

        File destino = chooser.showSaveDialog(this.getScene().getWindow());
        if (destino == null) return; // el usuario canceló el diálogo

        try {
            BoletoPdfExporter.exportar(boleto, destino);
            mostrarAlerta("PDF generado", "El boleto se guardó correctamente en:\n" + destino.getAbsolutePath(),
                Alert.AlertType.INFORMATION);
        } catch (IOException ex) {
            mostrarAlerta("Error al exportar", "No se pudo generar el PDF: " + ex.getMessage(),
                Alert.AlertType.ERROR);
            ex.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Devuelve el estilo CSS según el estado del boleto.
     * Verde para CONFIRMADO, Naranja para PENDIENTE.
     */
    private String obtenerEstiloEstado(Boleto.EstadoBoleto estado) {
        String baseStyle = "-fx-background-radius: 15; -fx-font-weight: bold; -fx-font-size: 10px; ";

        if (estado == Boleto.EstadoBoleto.CONFIRMADO) {
            return baseStyle + "-fx-background-color: rgba(46, 204, 113, 0.2); -fx-text-fill: #2ecc71; -fx-border-color: #2ecc71; -fx-border-width: 1;";
        } else {
            return baseStyle + "-fx-background-color: rgba(230, 126, 34, 0.2); -fx-text-fill: #e67e22; -fx-border-color: #e67e22; -fx-border-width: 1;";
        }
    }
}