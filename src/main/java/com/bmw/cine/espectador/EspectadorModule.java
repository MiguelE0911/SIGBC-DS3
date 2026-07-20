package com.bmw.cine.espectador;

import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.util.VentanaUtil;
import com.bmw.cine.espectador.controller.MainWindowController;
import com.bmw.cine.espectador.view.MainWindowView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EspectadorModule {

    public static void iniciar(Stage stage, UsuarioDTO usuarioActivo) {
        MainWindowView vistaPrincipal = new MainWindowView();
        new MainWindowController(vistaPrincipal, usuarioActivo, stage);

        Scene escena = new Scene(vistaPrincipal.getRootLayout(), 1200, 800);
        escena.getStylesheets().addAll(
                MainWindowView.class.getResource("/css/tema-global.css").toExternalForm(),
                MainWindowView.class.getResource("/css/header-principal.css").toExternalForm()
        );

        VentanaUtil.mostrar(stage, escena, "Cinema BMW - Cartelera");
    }
}