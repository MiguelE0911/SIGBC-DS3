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

/**
 * Pantalla intermedia que aparece después de un login exitoso, ANTES de
 * entrar a cualquier módulo (Espectador, Personal, Administrador).
 *
 * Reglas de negocio (ver Meta del flujo):
 *  - Espectador NUNCA ve esta pantalla: entra directo a Cartelera.
 *  - Personal ve 2 tarjetas: Cartelera / Taquilla.
 *  - Administrador ve 3 tarjetas: Cartelera / Taquilla / Panel de Administrador.
 *  - Si Personal o Administrador entra a "Cartelera", esa instancia se
 *    comporta EXACTAMENTE igual que la de un Espectador normal (compra
 *    para sí mismo) — no se le pasan permisos de staff a esa vista.
 *
 * TODO: los métodos entrarA*() de abajo son placeholders. Cuando existan
 * los lanzadores reales de cada módulo, reemplazar cada Alert por la
 * llamada correspondiente (ver comentarios).
 */
public class SelectorModulo {
    public static void iniciar(Stage stage, UsuarioDTO usuarioActivo) {
        int rol = usuarioActivo.getRol();
        if (rol == Usuario.ROL_ESPECTADOR) { // Regla de negocio: el Espectador nunca ve el selector.
            entrarACartelera(stage, usuarioActivo);
            return;
        }
        List<OpcionModulo> opciones = construirOpciones(stage, usuarioActivo, rol);
        mostrarPantalla(stage, usuarioActivo, opciones);
    }

    // Construcción de las opciones según el rol
    private static List<OpcionModulo> construirOpciones(Stage stage, UsuarioDTO usuarioActivo, int rol) {
        List<OpcionModulo> opciones = new ArrayList<>();
        opciones.add(new OpcionModulo("\uD83C\uDFAC", "Cartelera", "Compra boletos para ti mismo",
                () -> entrarACartelera(stage, usuarioActivo)
        ));
        opciones.add(new OpcionModulo("\uD83C\uDFAB", "Taquilla", "Atiende solicitudes y emite boletos",
                () -> entrarATaquilla(stage, usuarioActivo)
        ));
        if (rol == Usuario.ROL_ADMINISTRADOR) {
            opciones.add(new OpcionModulo("\u2699", "Panel de Administrador", "Gestiona usuarios, roles y reportes",
                    () -> entrarAAdmin(stage, usuarioActivo)
            ));
        }
        return opciones;
    }


    // Construcción visual
    private static void mostrarPantalla(Stage stage, UsuarioDTO usuarioActivo, List<OpcionModulo> opciones) {
        VBox encabezado = construirEncabezado(usuarioActivo);

        FlowPane tarjetas = new FlowPane();
        tarjetas.getStyleClass().add("contenedor-tarjetas");
        tarjetas.setAlignment(Pos.CENTER);
        tarjetas.setHgap(28);
        tarjetas.setVgap(28);
        for (OpcionModulo opcion : opciones) {
            tarjetas.getChildren().add(construirTarjeta(opcion));
        }

        BorderPane raiz = new BorderPane();
        raiz.getStyleClass().add("selector-fondo");
        raiz.setTop(encabezado);
        raiz.setCenter(tarjetas);
        BorderPane.setAlignment(tarjetas, Pos.CENTER);

        Scene escena = new Scene(raiz, 900, 600);
        escena.getStylesheets().add(
                SelectorModulo.class.getResource("/css/selector-modulo.css").toExternalForm()
        );

        stage.setTitle("Selector de Módulo");
        stage.setScene(escena);
        stage.show();
    }

    private static VBox construirEncabezado(UsuarioDTO usuarioActivo) {
        Label titulo = new Label("¿A dónde quieres entrar?");
        titulo.getStyleClass().add("selector-titulo");

        Label subtitulo = new Label("Sesión iniciada como " + usuarioActivo.getNombre()
                + " · " + usuarioActivo.getNombreRol());
        subtitulo.getStyleClass().add("selector-subtitulo");

        VBox contenedor = new VBox(6, titulo, subtitulo);
        contenedor.setAlignment(Pos.CENTER);
        contenedor.setPadding(new Insets(48, 24, 32, 24));
        return contenedor;
    }

    private static VBox construirTarjeta(OpcionModulo opcion) {
        Label icono = new Label(opcion.getIcono());
        icono.getStyleClass().add("tarjeta-icono");

        Label titulo = new Label(opcion.getTitulo());
        titulo.getStyleClass().add("tarjeta-titulo");
        titulo.setWrapText(true);
        titulo.setMaxWidth(190);
        titulo.setAlignment(Pos.CENTER);
        titulo.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label descripcion = new Label(opcion.getDescripcion());
        descripcion.getStyleClass().add("tarjeta-descripcion");
        descripcion.setWrapText(true);
        descripcion.setMaxWidth(180);
        descripcion.setAlignment(Pos.CENTER);

        Button entrar = new Button("Entrar");
        entrar.getStyleClass().add("tarjeta-boton");
        entrar.setOnAction(e -> opcion.getAccion().run());

        VBox tarjeta = new VBox(10, icono, titulo, descripcion, entrar);
        tarjeta.getStyleClass().add("tarjeta");
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setPrefWidth(230);
        tarjeta.setMinHeight(260);
        return tarjeta;
    }

    // Destinos (placeholders hasta que existan los módulos reales)
    private static void entrarACartelera(Stage stage, UsuarioDTO usuarioActivo) {
        // TODO: reemplazar por EspectadorModule.iniciar(stage, usuarioActivo);
        // Importante: esta llamada debe ser SIEMPRE la misma, sin importar
        // si quien entra es Espectador, Personal o Administrador — la vista
        // de Cartelera no debe recibir ni usar permisos de staff.
        mostrarPendiente("Cartelera");
    }

    private static void entrarATaquilla(Stage stage, UsuarioDTO usuarioActivo) {
        // TODO: reemplazar por PersonalModule.iniciar(stage, usuarioActivo);
        mostrarPendiente("Taquilla");
    }

    private static void entrarAAdmin(Stage stage, UsuarioDTO usuarioActivo) {
        // TODO: reemplazar por AdminModule.iniciar(stage, usuarioActivo);
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