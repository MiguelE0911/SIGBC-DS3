package com.bmw.cine;

import com.bmw.cine.app.SessionRouter;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Usuario;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        System.out.println("Main principal");
        launch(args); // arranca JavaFX y llama a start(Stage)
    }

    @Override
    public void start(Stage stage) {
        // TODO: cuando exista el login real, reemplazar esto por LoginView.mostrar(stage)
        // y que sea el LoginController quien, al autenticar (UsuarioDAO.autenticar),
        // reciba el UsuarioDTO ya resuelto y llame a SelectorModulo.iniciar(...)
        // (o directo a EspectadorModule si el rol es Espectador).
        UsuarioDTO usuarioPrueba = new UsuarioDTO(1, "Ana Torres", "ana@test.com", "ana.torres", Usuario.ROL_ADMINISTRADOR, true);
        SessionRouter.enrutar(stage, usuarioPrueba);
    }
}