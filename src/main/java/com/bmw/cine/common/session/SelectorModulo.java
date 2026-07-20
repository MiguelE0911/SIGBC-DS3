package com.bmw.cine.common.session;

import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Usuario;
import com.bmw.cine.common.view.SelectorModuloView;

import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class SelectorModulo {
    public static void iniciar(Stage stage, UsuarioDTO usuarioActivo) {
        int rol = usuarioActivo.getRol();
        if (rol == Usuario.ROL_ESPECTADOR) {
            entrarACartelera(stage, usuarioActivo);
            return;
        }
        List<OpcionModulo> opciones = construirOpciones(stage, usuarioActivo, rol);
        new SelectorModuloView().mostrar(stage, usuarioActivo, opciones);
    }

    private static List<OpcionModulo> construirOpciones(Stage stage, UsuarioDTO usuarioActivo, int rol) {
        List<OpcionModulo> opciones = new ArrayList<>();
        opciones.add(new OpcionModulo("\uD83C\uDFAC", "Cartelera", "Compra boletos para ti mismo",
                () -> entrarACartelera(stage, usuarioActivo)
        ));
        opciones.add(new OpcionModulo("\uD83C\uDFAB", "Taquilla", "Atiende solicitudes y emite boletos",
                () -> entrarATaquilla(stage, usuarioActivo)
        ));
        if (rol == Usuario.ROL_ADMINISTRADOR) {
            opciones.add(new OpcionModulo("\u2699", "Panel de Administrator", "Gestiona usuarios, roles y reportes",
                    () -> entrarAAdmin(stage, usuarioActivo)
            ));
        }
        return opciones;
    }

    private static void entrarACartelera(Stage stage, UsuarioDTO usuarioActivo) {
        com.bmw.cine.espectador.EspectadorModule.iniciar(stage, usuarioActivo);
    }

    private static void entrarATaquilla(Stage stage, UsuarioDTO usuarioActivo) {
        com.bmw.cine.personal.PersonalModule.iniciar(stage, usuarioActivo);
    }

    private static void entrarAAdmin(Stage stage, UsuarioDTO usuarioActivo) {
        com.bmw.cine.administrador.AdminModule.iniciar(stage, usuarioActivo);
    }
}