package com.bmw.cine.administrador;

import com.bmw.cine.administrador.controller.AdminController;
import com.bmw.cine.administrador.view.AdminView;

import javafx.application.Application;
import javafx.stage.Stage;

public class AdminMain extends Application {

    @Override
    public void start(Stage primaryStage) {

        AdminView vista = new AdminView();

        new AdminController(vista, primaryStage);

        vista.mostrar(primaryStage);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
