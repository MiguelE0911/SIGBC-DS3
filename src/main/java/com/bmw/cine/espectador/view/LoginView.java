package com.bmw.cine.espectador.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Vista encargada de la interfaz gráfica del inicio de sesión (Login)
 * para el Módulo del Espectador.
 * @author Wilma
 * @version 1.0
 */
public class LoginView {

    // Componentes de la interfaz que el controlador necesitará leer o escuchar
    private TextField txtUsuario;
    private PasswordField txtPassword;
    private Button btnIngresar;
    private Hyperlink lnkRegistrarse;
    private Scene escena;

    /**
     * Constructor de la vista. Inicializa y estructura todos los componentes visuales.
     */
    public LoginView() {
        inicializarComponentes();
    }

    /**
     * Crea los elementos gráficos, aplica los estilos y los organiza dentro del contenedor principal.
     */
    private void inicializarComponentes() {
        // 1. Título de la ventana
        Label lblTitulo = new Label("Iniciar Sesión");
        lblTitulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #f4e8d0;");

        // 2. Subtítulo o descripción corta
        Label lblSubtitulo = new Label("Módulo del Espectador");
        lblSubtitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: #b8a9c9;");

        // 3. Campo de texto para el Usuario o Correo
        txtUsuario = new TextField();
        txtUsuario.setPromptText("Usuario o Correo electrónico");
        txtUsuario.setPrefHeight(40);
        txtUsuario.setStyle(
            "-fx-background-color: #241a30; " +
            "-fx-text-fill: #f4e8d0; " +
            "-fx-prompt-text-fill: #b8a9c9; " +
            "-fx-background-radius: 8; " +
            "-fx-border-radius: 8; " +
            "-fx-border-color: #3a2b4a; " +
            "-fx-border-width: 1;"
        );

        // 4. Campo de texto para la Contraseña
        txtPassword = new PasswordField();
        txtPassword.setPromptText("Contraseña");
        txtPassword.setPrefHeight(40);
        txtPassword.setStyle(
            "-fx-background-color: #241a30; " +
            "-fx-text-fill: #f4e8d0; " +
            "-fx-prompt-text-fill: #b8a9c9; " +
            "-fx-background-radius: 8; " +
            "-fx-border-radius: 8; " +
            "-fx-border-color: #3a2b4a; " +
            "-fx-border-width: 1;"
        );

        // 5. Botón de Ingreso (Acción Principal - Color Dorado VIP)
        btnIngresar = new Button("Ingresar");
        btnIngresar.setPrefHeight(42);
        btnIngresar.setMaxWidth(Double.MAX_VALUE); // Ocupa todo el ancho disponible
        btnIngresar.setStyle(
            "-fx-background-color: #d4af37; " +
            "-fx-text-fill: #1b1224; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 14px; " +
            "-fx-background-radius: 20; " +
            "-fx-cursor: hand;"
        );

        // Efecto visual interactivo (Hover) para el botón en línea
        btnIngresar.setOnMouseEntered(e -> btnIngresar.setStyle(
            "-fx-background-color: #e6c34f; -fx-text-fill: #1b1224; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 20; -fx-cursor: hand;"
        ));
        btnIngresar.setOnMouseExited(e -> btnIngresar.setStyle(
            "-fx-background-color: #d4af37; -fx-text-fill: #1b1224; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 20; -fx-cursor: hand;"
        ));

        // 6. Enlace para redirigir al Registro
        lnkRegistrarse = new Hyperlink("¿No tienes cuenta? Regístrate aquí");
        lnkRegistrarse.setStyle("-fx-text-fill: #b8a9c9; -fx-font-size: 12px; -fx-underline: false;");
        lnkRegistrarse.setOnMouseEntered(e -> lnkRegistrarse.setStyle("-fx-text-fill: #f4e8d0; -fx-font-size: 12px; -fx-underline: true;"));
        lnkRegistrarse.setOnMouseExited(e -> lnkRegistrarse.setStyle("-fx-text-fill: #b8a9c9; -fx-font-size: 12px; -fx-underline: false;"));

        // 7. Contenedor Principal (VBox con degradado de fondo oficial)
        VBox root = new VBox(20); // 20px de espacio vertical entre cada elemento
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1b1224, #100b16);");

        // Añadir todos los componentes al contenedor en orden
        root.getChildren().addAll(lblTitulo, lblSubtitulo, txtUsuario, txtPassword, btnIngresar, lnkRegistrarse);

        // 8. Crear la escena de la ventana
        escena = new Scene(root, 380, 480);
    }

    /**
     * Muestra la vista en el escenario principal provisto.
     * * @param stage El escenario (Stage) de JavaFX donde se renderizará la vista.
     */
    public void mostrar(Stage stage) {
        stage.setTitle("Cinema - Iniciar Sesión");
        stage.setScene(escena);
        stage.setResizable(false); // Mantiene fijo el tamaño del Login
        stage.show();
    }

    // --- MÉTODOS GETTERS PARA EL CONTROLADOR ---

    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getPassword() {
        return txtPassword.getText();
    }

    public Button getBtnIngresar() {
        return btnIngresar;
    }

    public Hyperlink getLnkRegistrarse() {
        return lnkRegistrarse;
    }
}