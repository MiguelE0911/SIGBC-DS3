package com.bmw.cine.common.session;

import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Usuario;
import com.bmw.cine.common.session.OpcionModulo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

// Pantalla intermedia que aparece después de un login exitoso.
public class SelectorModulo {
    public static void iniciar(Stage stage, UsuarioDTO usuarioActivo) {
        int rol = usuarioActivo.getRol();
        if (rol == Usuario.ROL_ESPECTADOR) {
            entrarACartelera(stage, usuarioActivo);
            return;
        }
        List<OpcionModulo> opciones = construirOpciones(stage, usuarioActivo, rol);
        mostrarPantalla(stage, usuarioActivo, opciones);
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

    // Construcción visual
    private static void mostrarPantalla(Stage stage, UsuarioDTO usuarioActivo, List<OpcionModulo> opciones) {
        VBox encabezado = construirEncabezado(usuarioActivo);

        FlowPane tarjetas = new FlowPane();
        tarjetas.setAlignment(Pos.CENTER);
        tarjetas.setHgap(28);
        tarjetas.setVgap(28);
        tarjetas.setPadding(new Insets(0, 40, 48, 40));

        for (OpcionModulo opcion : opciones) {
            tarjetas.getChildren().add(construirTarjeta(opcion));
        }

        BorderPane raiz = new BorderPane();
        raiz.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 0% 100%, #1b1224, #100b16);");
        raiz.setTop(encabezado);
        raiz.setCenter(tarjetas);
        BorderPane.setAlignment(tarjetas, Pos.CENTER);

        Scene escena = new Scene(raiz, 900, 600);

        stage.setTitle("Selector de Módulo");
        stage.setScene(escena);
        stage.show();
    }

    private static VBox construirEncabezado(UsuarioDTO usuarioActivo) {
        Label titulo = new Label("¿A dónde quieres entrar?");
        titulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #f4e8d0; -fx-font-family: 'Segoe UI';");

        Label subtitulo = new Label("Sesión iniciada como " + usuarioActivo.getNombre()
                + " · " + usuarioActivo.getNombreRol());
        subtitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: #b8a9c9; -fx-font-family: 'Segoe UI';");

        VBox contenedor = new VBox(6, titulo, subtitulo);
        contenedor.setAlignment(Pos.CENTER);
        contenedor.setPadding(new Insets(48, 24, 32, 24));
        return contenedor;
    }

    private static VBox construirTarjeta(OpcionModulo opcion) {
        Label icono = new Label(opcion.getIcono());
        icono.setStyle("-fx-font-size: 42px;");

        Label titulo = new Label(opcion.getTitulo());
        titulo.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #f4e8d0; -fx-font-family: 'Segoe UI';");
        titulo.setWrapText(true);
        titulo.setMaxWidth(190);
        titulo.setAlignment(Pos.CENTER);
        titulo.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label descripcion = new Label(opcion.getDescripcion());
        descripcion.setStyle("-fx-font-size: 12px; -fx-text-fill: #b8a9c9; -fx-font-family: 'Segoe UI';");
        descripcion.setWrapText(true);
        descripcion.setMaxWidth(180);
        descripcion.setAlignment(Pos.CENTER);
        descripcion.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Button entrar = new Button("Entrar");

        // Estilos del botón base y hover mediante eventos de Java
        String btnNormal = "-fx-background-color: #d4af37; -fx-text-fill: #1b1224; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 8 24 8 24; -fx-cursor: hand; -fx-font-family: 'Segoe UI';";
        String btnHover = "-fx-background-color: #e6c34f; -fx-text-fill: #1b1224; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 8 24 8 24; -fx-cursor: hand; -fx-font-family: 'Segoe UI';";
        entrar.setStyle(btnNormal);
        entrar.setOnMouseEntered(e -> entrar.setStyle(btnHover));
        entrar.setOnMouseExited(e -> entrar.setStyle(btnNormal));
        entrar.setOnAction(e -> opcion.getAccion().run());

        VBox tarjeta = new VBox(14, icono, titulo, descripcion, entrar);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setPrefWidth(230);
        tarjeta.setMinHeight(260);

        // Estilos de la tarjeta base y hover mediante eventos de Java
        String cardNormal = "-fx-background-color: #241a30; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: #3a2b4a; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.45), 14, 0, 0, 6); -fx-cursor: hand;";
        String cardHover = "-fx-background-color: #2e2140; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: #d4af37; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(212,175,55,0.35), 18, 0, 0, 8); -fx-cursor: hand;";

        tarjeta.setStyle(cardNormal);
        tarjeta.setOnMouseEntered(e -> tarjeta.setStyle(cardHover));
        tarjeta.setOnMouseExited(e -> tarjeta.setStyle(cardNormal));

        return tarjeta;
    }

    private static void entrarACartelera(Stage stage, UsuarioDTO usuarioActivo) {
        mostrarPendiente("Cartelera");
    }

    private static void entrarATaquilla(Stage stage, UsuarioDTO usuarioActivo) {
        com.bmw.cine.personal.PersonalModule.iniciar(stage, usuarioActivo);
    }

    private static void entrarAAdmin(Stage stage, UsuarioDTO usuarioActivo) {
        mostrarPendiente("Panel de Administrador");
    }

    private static void mostrarPendiente(String nombreModulo) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Módulo pendiente");
        alerta.setHeaderText(null);
        alerta.setContentText("El módulo \"" + nombreModulo + "\" aún no está implementado.");
        alerta.showAndWait();
    }
}