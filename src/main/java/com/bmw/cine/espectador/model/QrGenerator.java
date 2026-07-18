package com.bmw.cine.espectador.model;

import java.awt.image.BufferedImage;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image; // Esta línea dejará de marcar error tras el paso 1

/**
 * Utilidad para generar códigos QR.
 */
public class QrGenerator {

    public static Image generarImagenQr(String contenido) {
        try {
            // Generamos la matriz del QR
            BitMatrix matrix = new MultiFormatWriter().encode(contenido, BarcodeFormat.QR_CODE, 200, 200);
            
            // Convertimos la matriz a una BufferedImage (AWT)
            BufferedImage bImage = MatrixToImageWriter.toBufferedImage(matrix);
            
            // Convertimos la BufferedImage a una Image de JavaFX usando el módulo swing
            return SwingFXUtils.toFXImage(bImage, null);
            
        } catch (WriterException e) {
            // Manejo específico de la excepción del generador [7]
            System.err.println("Error al codificar el QR: " + e.getMessage());
            return null;
        }
    }
}