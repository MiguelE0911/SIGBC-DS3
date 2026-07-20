package com.bmw.cine.common.util;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

public class PosterFileUtil {

    private static final String CARPETA_POSTERS = "posters";

    // Resuelve la ruta al Escritorio del usuario actual, sin importar el SO.
    private static Path obtenerCarpetaPosters() {
        Path escritorio = Paths.get(System.getProperty("user.home"), "Desktop");
        // En algunos sistemas en español (Windows/Linux localizados) la carpeta se llama "Escritorio"
        if (!Files.isDirectory(escritorio)) {
            Path alternativa = Paths.get(System.getProperty("user.home"), "Escritorio");
            if (Files.isDirectory(alternativa)) {
                escritorio = alternativa;
            }
        }
        return escritorio.resolve(CARPETA_POSTERS);
    }

    public static Optional<File> elegirArchivo(Window ventanaPadre) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleccionar póster");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
        return Optional.ofNullable(chooser.showOpenDialog(ventanaPadre));
    }

    // Copia el archivo elegido a <Escritorio>/posters con nombre único y devuelve el nombre.
    public static String guardarPoster(File origen) {
        try {
            Path carpeta = obtenerCarpetaPosters();
            Files.createDirectories(carpeta);

            String extension = obtenerExtension(origen.getName());
            String nombreDestino = UUID.randomUUID() + extension;
            Path destino = carpeta.resolve(nombreDestino);

            Files.copy(origen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
            return nombreDestino;
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el póster: " + e.getMessage(), e);
        }
    }

    public static Image cargarImagen(String nombrePoster) {
        if (nombrePoster == null || nombrePoster.isBlank()) return null;
        File archivo = obtenerCarpetaPosters().resolve(nombrePoster).toFile();
        return archivo.exists() ? new Image(archivo.toURI().toString()) : null;
    }

    private static String obtenerExtension(String nombreArchivo) {
        int punto = nombreArchivo.lastIndexOf('.');
        return punto >= 0 ? nombreArchivo.substring(punto) : "";
    }
}