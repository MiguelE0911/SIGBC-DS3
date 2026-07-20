package com.bmw.cine.common.util;

import javafx.scene.Scene;
import javafx.stage.Stage;

// Centraliza cómo se muestra cada pantalla para que la ventana
// siempre aparezca centrada, sin importar el tamaño de la Scene.
public class VentanaUtil {

    public static void mostrar(Stage stage, Scene escena, String titulo) {
        stage.setTitle(titulo);
        stage.setScene(escena);
        stage.centerOnScreen();
        stage.show();
    }
}