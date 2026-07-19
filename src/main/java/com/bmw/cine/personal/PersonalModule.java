package com.bmw.cine.personal;

import java.io.IOException;

import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.view.HeaderPrincipalController;
import com.bmw.cine.personal.view.CarteleraCrudView;
import com.bmw.cine.personal.view.EmitirBoletoView;
import com.bmw.cine.personal.view.TaquillaView;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

// Lanzador del módulo de Personal usando el Header FXML original.
public class PersonalModule {

    @SuppressWarnings("CallToPrintStackTrace")
    public static void iniciar(Stage stage, UsuarioDTO usuarioActivo) {
        try {
            stage.setTitle("Panel de Personal - Cine BMW");

            BorderPane contenedorPrincipal = new BorderPane();
            contenedorPrincipal.getStyleClass().add("panel-fondo");

            // CARGAR HEADER DESDE EL FXML
            FXMLLoader loader = new FXMLLoader(PersonalModule.class.getResource("/common/view/HeaderPrincipal.fxml"));
            Parent header = loader.load();
            HeaderPrincipalController headerCtrl = loader.getController();

            // CONFIGURAR DATOS DEL HEADER
            headerCtrl.configurar("Cine BMW", usuarioActivo, true);

            // INSTANCIAR LAS VISTAS
            TaquillaView vistaTaquilla = new TaquillaView(usuarioActivo);
            CarteleraCrudView vistaCartelera = new CarteleraCrudView();
            EmitirBoletoView vistaEmitir = new EmitirBoletoView(usuarioActivo);

            // AGREGAR BOTONES DE NAVEGACIÓN
            Button btnTaquilla = headerCtrl.agregarBotonNav("Taquilla", () -> {
                contenedorPrincipal.setCenter(vistaTaquilla);
            });
            Button btnCartelera = headerCtrl.agregarBotonNav("Cartelera", () -> {
                contenedorPrincipal.setCenter(vistaCartelera);
            });
            Button btnEmitir = headerCtrl.agregarBotonNav("Emitir boleto", () -> {
                contenedorPrincipal.setCenter(vistaEmitir);
            });

            // CONFIGURAR ACCIONES DEL MENÚ DESPLEGABLE
            headerCtrl.setOnCerrarSesion(() -> headerCtrl.setOnCerrarSesion(() ->com.bmw.cine.app.SessionRouter.cerrarSesion(stage)));

            headerCtrl.setOnCambiarSeccion(() -> com.bmw.cine.common.session.SelectorModulo.iniciar(stage, usuarioActivo));

            //  ESTADO INICIAL DE LA PANTALLA
            contenedorPrincipal.setTop(header);
            contenedorPrincipal.setCenter(vistaTaquilla);
            headerCtrl.marcarActivo(btnTaquilla);

            //  CONFIGURAR ESCENA Y CSS ORIGINALES
            Scene escena = new Scene(contenedorPrincipal, 1024, 768);

            // Variables de diseño están aquí, por lo que es vital cargarlos en orden
            escena.getStylesheets().add(PersonalModule.class.getResource("/css/tema-global.css").toExternalForm());
            escena.getStylesheets().add(PersonalModule.class.getResource("/css/panel-comun.css").toExternalForm());
            escena.getStylesheets().add(PersonalModule.class.getResource("/css/header-principal.css").toExternalForm());

            stage.setScene(escena);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarPendiente("Error al cargar la interfaz visual: " + e.getMessage());
        }
    }

    private static void mostrarPendiente(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Pendiente");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}