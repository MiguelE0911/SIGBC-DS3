package com.bmw.cine.administrador;

import com.bmw.cine.administrador.controller.AdminController;
import com.bmw.cine.administrador.view.AdminView;
import com.bmw.cine.common.dto.UsuarioDTO;
import javafx.stage.Stage;

public class AdminModule {
    public static void iniciar(Stage stage, UsuarioDTO usuarioActivo) {
        AdminView vista = new AdminView(usuarioActivo);

        new AdminController(
                vista,
                stage,
                usuarioActivo
        );
        vista.mostrar(stage);
    }
}
