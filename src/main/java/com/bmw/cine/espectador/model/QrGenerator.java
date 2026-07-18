package com.bmw.cine.espectador.model;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;

public class QrGenerator {

    /**
     * Genera una imagen QR para ser usada en interfaces JavaFX.
     * @param contenido El texto a codificar (ej. ID Compra + Función).
     * @return Objeto Image de JavaFX.
     */
    public static Image generarImagenQr(String contenido) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(contenido, BarcodeFormat.QR_CODE, 200, 200);
            BufferedImage bImage = MatrixToImageWriter.toBufferedImage(matrix);
            return SwingFXUtils.toFXImage(bImage, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}