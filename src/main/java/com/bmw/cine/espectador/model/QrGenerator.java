package com.bmw.cine.espectador.model;

import java.awt.image.BufferedImage;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class QrGenerator {

    public static Image generarImagenQr(String contenido) {
        BufferedImage bImage = generarBufferedImage(contenido, 200);
        return bImage != null ? SwingFXUtils.toFXImage(bImage, null) : null;
    }

    /** Genera el QR como BufferedImage puro (AWT), útil para incrustar en PDFs. */
    public static BufferedImage generarBufferedImage(String contenido, int tamano) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(contenido, BarcodeFormat.QR_CODE, tamano, tamano);
            return MatrixToImageWriter.toBufferedImage(matrix);
        } catch (WriterException e) {
            System.err.println("Error al codificar el QR: " + e.getMessage());
            return null;
        }
    }
}