package com.bmw.cine.common.session;

import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Usuario;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Lanzador TEMPORAL para probar visualmente el Selector de Módulo sin
 * depender de que exista login todavía.
 * Cambia la constante ROL_DE_PRUEBA para ver cómo se ve con Personal
 * (2 tarjetas) o Administrador (3 tarjetas). No usar esta clase una vez
 * exista el login real: bórrala o muévela a un paquete de pruebas.
 */
public class SelectorModuloDemo extends Application {

    // Cambia esto entre Usuario.ROL_PERSONAL y Usuario.ROL_ADMINISTRADOR para probar ambos casos.
    // (Usuario.ROL_ESPECTADOR no debería mostrar el selector — ver SelectorModulo.iniciar)
    private static final int ROL_DE_PRUEBA = Usuario.ROL_ADMINISTRADOR;

    @Override
    public void start(Stage stage) {
        UsuarioDTO usuarioPrueba = new UsuarioDTO(1, "Ana Torres", "ana@test.com", "ana.torres", ROL_DE_PRUEBA, true);
        SelectorModulo.iniciar(stage, usuarioPrueba);
    }

    public static void main(String[] args) {
        launch(args);
    }
}