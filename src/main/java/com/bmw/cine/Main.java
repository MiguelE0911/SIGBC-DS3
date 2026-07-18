package com.bmw.cine;

import com.bmw.cine.common.model.Usuario;
import com.bmw.cine.common.session.SelectorModulo;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args); // arranca JavaFX y llama a start(Stage)
    }

    @Override
    public void start(Stage stage) {
        // TODO: cuando exista el login real, reemplazar esto por LoginView.mostrar(stage)
        // y que sea el LoginController quien, al autenticar, llame a SelectorModulo.iniciar(...)
        // (o directo a EspectadorModule si el rol es Espectador).
        Usuario usuarioPrueba = new Usuario(1, "Lamin Yamal", "lamin@test.com", Usuario.ROL_ADMINISTRADOR);
        SelectorModulo.iniciar(stage, usuarioPrueba);
    }
}