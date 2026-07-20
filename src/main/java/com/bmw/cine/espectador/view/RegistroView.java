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

public class RegistroView {

    // Componentes de la interfaz gráfica necesarios para el controlador
    private TextField txtNombre;
    private TextField txtCorreo;
    private TextField txtUsuario;
    private PasswordField txtPassword;
    private Button btnRegistrar;
    private Hyperlink lnkVolverLogin;
    private Scene escena;

    // Constructor de la vista. Inicializa todos los controles y aplica los estilos.
    public RegistroView() {
        inicializarComponentes();
    }

    // Crea los elementos visuales, define la paleta de colores y estructura el contenedor.
    private void inicializarComponentes() {
        // 1. Título principal de la ventana
        Label lblTitulo = new Label("Crear Cuenta");
        lblTitulo.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #f4e8d0;");

        // 2. Subtítulo descriptivo
        Label lblSubtitulo = new Label("Únete a la experiencia VIP del cine");
        lblSubtitulo.setStyle("-fx-font-size: 13px; -fx-text-fill: #b8a9c9;");

        // Estilo común reutilizable para todos los cuadros de texto
        String estiloInput = 
            "-fx-background-color: #241a30; " +
            "-fx-text-fill: #f4e8d0; " +
            "-fx-prompt-text-fill: #b8a9c9; " +
            "-fx-background-radius: 8; " +
            "-fx-border-radius: 8; " +
            "-fx-border-color: #3a2b4a; " +
            "-fx-border-width: 1;";

        // 3. Campo: Nombre Completo
        txtNombre = new TextField();
        txtNombre.setPromptText("Nombre completo");
        txtNombre.setPrefHeight(38);
        txtNombre.setStyle(estiloInput);

        // 4. Campo: Correo Electrónico
        txtCorreo = new TextField();
        txtCorreo.setPromptText("Correo electrónico");
        txtCorreo.setPrefHeight(38);
        txtCorreo.setStyle(estiloInput);

        // 5. Campo: Nombre de Usuario
        txtUsuario = new TextField();
        txtUsuario.setPromptText("Nombre de usuario");
        txtUsuario.setPrefHeight(38);
        txtUsuario.setStyle(estiloInput);

        // 6. Campo: Contraseña
        txtPassword = new PasswordField();
        txtPassword.setPromptText("Contraseña nueva");
        txtPassword.setPrefHeight(38);
        txtPassword.setStyle(estiloInput);

        // 7. Botón de Registro (Acción Principal - Dorado VIP)
        btnRegistrar = new Button("Registrarse");
        btnRegistrar.setPrefHeight(40);
        btnRegistrar.setMaxWidth(Double.MAX_VALUE); // Ancho completo dentro del VBox
        btnRegistrar.setStyle(
            "-fx-background-color: #d4af37; " +
            "-fx-text-fill: #1b1224; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 14px; " +
            "-fx-background-radius: 20; " +
            "-fx-cursor: hand;"
        );

        // Efectos dinámicos (Hover) en línea para el botón
        btnRegistrar.setOnMouseEntered(e -> btnRegistrar.setStyle(
            "-fx-background-color: #e6c34f; -fx-text-fill: #1b1224; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 20; -fx-cursor: hand;"
        ));
        btnRegistrar.setOnMouseExited(e -> btnRegistrar.setStyle(
            "-fx-background-color: #d4af37; -fx-text-fill: #1b1224; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 20; -fx-cursor: hand;"
        ));

        // 8. Enlace para regresar a la vista anterior
        lnkVolverLogin = new Hyperlink("¿Ya tienes cuenta? Inicia sesión aquí");
        lnkVolverLogin.setStyle("-fx-text-fill: #b8a9c9; -fx-font-size: 12px; -fx-underline: false;");
        lnkVolverLogin.setOnMouseEntered(e -> lnkVolverLogin.setStyle("-fx-text-fill: #f4e8d0; -fx-font-size: 12px; -fx-underline: true;"));
        lnkVolverLogin.setOnMouseExited(e -> lnkVolverLogin.setStyle("-fx-text-fill: #b8a9c9; -fx-font-size: 12px; -fx-underline: false;"));

        // 9. Estructuración del Contenedor Principal (VBox)
        VBox root = new VBox(18); // Separación vertical consistente
        root.setPadding(new Insets(35, 40, 40, 40));
        root.setAlignment(Pos.CENTER);
        // Mismo degradado lineal de fondo que el Login
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1b1224, #100b16);");

        // Agregamos todos los componentes en orden descendente
        root.getChildren().addAll(lblTitulo, lblSubtitulo, txtNombre, txtCorreo, txtUsuario, txtPassword, btnRegistrar, lnkVolverLogin);

        // 10. Escena con dimensiones ideales para el formulario extendido
        escena = new Scene(root, 380, 520);
    }

    public void mostrar(Stage stage) {
        stage.setTitle("Cinema - Crear Cuenta Espectador");
        stage.setScene(escena);
        stage.setResizable(false);
        stage.show();
    }

    //  MÉTODOS GETTERS PARA EXTRACCIÓN DE DATOS DESDE EL CONTROLADOR
    public String getNombre() {return txtNombre.getText().trim();}
    public String getCorreo() {return txtCorreo.getText().trim();}
    public String getUsuario() {return txtUsuario.getText().trim();}
    public String getPassword() {return txtPassword.getText();}
    public Button getBtnRegistrar() {return btnRegistrar;}
    public Hyperlink getLnkVolverLogin() {return lnkVolverLogin;}
}