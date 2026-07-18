package com.bmw.cine.app;

import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Usuario;
import com.bmw.cine.common.session.SelectorModulo;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Única clase que conoce a los 3 módulos (Espectador, Personal,
 * Administrador). Nadie más debe importar EspectadorModule,
 * PersonalModule ni AdminModule directamente — todos pasan por aquí.
 *
 * Se llama UNA sola vez, justo después de un login exitoso:
 *   LoginController -> UsuarioDAO.autenticar() -> SessionRouter.enrutar()
 *
 * Regla de negocio (la misma que ya vive en SelectorModulo, pero aquí
 * decide el punto de entrada, no qué tarjetas mostrar):
 *  - Espectador: nunca ve el selector, entra directo a Cartelera.
 *  - Personal / Administrador: pasan por el Selector de Módulo.
 */

public class SessionRouter {

    public static void enrutar(Stage stage, UsuarioDTO usuarioActivo) {
        if (usuarioActivo.getRol() == Usuario.ROL_ESPECTADOR) {
            // TODO: reemplazar por EspectadorModule.iniciar(stage, usuarioActivo);
            mostrarPendiente("Cartelera (Espectador)"); // Eliminar esta linea cuando se haya creado el enlace a cartelera
            return;
        }
        SelectorModulo.iniciar(stage, usuarioActivo); // Personal y Administrador pasan por el selector.
    }

    // Eliminar este metodo cuando se haya creado el enlace a cartelera
    private static void mostrarPendiente(String nombreModulo) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Módulo pendiente");
        alerta.setHeaderText(null);
        alerta.setContentText("El módulo \"" + nombreModulo + "\" aún no está implementado.");
        alerta.showAndWait();
    }
}
