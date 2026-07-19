package com.bmw.cine.espectador;

import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dto.UsuarioDTO;

import javafx.stage.Stage;

/**
 * Utilidad para abrir el módulo del Espectador una vez autenticado.
 * NO es un punto de entrada de la aplicación (no extiende Application).
 * El único lanzador de la app es com.bmw.cine.Main.
 */
public class EspectadorModule {

    /**
     * Abre la vista principal del módulo del espectador, reutilizando
     * el stage ya existente.
     */
    public static void iniciar(Stage stage, UsuarioDTO usuarioDTO, UsuarioDAO usuarioDAO) {
        stage.setTitle("Cine BMW - Espectador: " + usuarioDTO.getNombre());
        // TODO: aquí instancias la vista principal del espectador
        // (cartelera, compra de boletos, etc.) y su controlador,
        // pasando usuarioDAO y usuarioDTO si los necesita.
        //
        // Ejemplo:
        // CarteleraView carteleraView = new CarteleraView();
        // new CarteleraController(carteleraView, usuarioDAO, usuarioDTO, stage);
        // carteleraView.mostrar(stage);
    }

    private EspectadorModule() {
        // Clase de utilidad, no instanciable
    }
}