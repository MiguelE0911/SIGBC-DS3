package com.bmw.cine.espectador.view;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.bmw.cine.common.dto.FuncionDTO;
import com.bmw.cine.common.model.Pelicula;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

public class DetallePeliculaView extends VBox {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final ImageView imgPoster;
    private final Label lblTitulo;
    private final Label lblInfo;
    private final Label lblSinopsis;
    private final ComboBox<FuncionDTO> cbHorarios;
    private final Spinner<Integer> spCantidad;
    private final Button btnComprar;
    private final Button btnCerrar;

    public DetallePeliculaView() {
        this.setStyle("-fx-background-color: #100b16;");
        this.setPrefWidth(760);

        btnCerrar = new Button("✕");
        btnCerrar.setStyle("-fx-background-color: transparent; -fx-text-fill: #b8a9c9; -fx-font-size: 16px; -fx-cursor: hand;");
        HBox barraSuperior = new HBox(btnCerrar);
        barraSuperior.setAlignment(Pos.CENTER_RIGHT);
        barraSuperior.setPadding(new Insets(12, 16, 0, 16));

        imgPoster = new ImageView();
        imgPoster.setFitWidth(260);
        imgPoster.setFitHeight(380);
        imgPoster.setPreserveRatio(false);
        imgPoster.setStyle("-fx-background-color: #241a30;");

        lblTitulo = new Label();
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #f4e8d0; -fx-font-family: 'Segoe UI';");
        lblTitulo.setWrapText(true);

        lblInfo = new Label();
        lblInfo.setStyle("-fx-font-size: 13px; -fx-text-fill: #d4af37; -fx-font-family: 'Segoe UI';");

        Label lblSinopsisTitulo = new Label("SINOPSIS");
        lblSinopsisTitulo.setStyle("-fx-font-size: 12px; -fx-text-fill: #b8a9c9; -fx-font-weight: bold; -fx-letter-spacing: 1px;");

        lblSinopsis = new Label();
        lblSinopsis.setStyle("-fx-font-size: 13px; -fx-text-fill: #e0d5c8; -fx-font-family: 'Segoe UI';");
        lblSinopsis.setWrapText(true);
        lblSinopsis.setTextAlignment(TextAlignment.LEFT);
        lblSinopsis.setMaxWidth(440);

        Label lblHorarioTitulo = new Label("HORARIO Y SALA");
        lblHorarioTitulo.setStyle("-fx-font-size: 12px; -fx-text-fill: #b8a9c9; -fx-font-weight: bold; -fx-letter-spacing: 1px;");

        cbHorarios = new ComboBox<>();
        cbHorarios.setPromptText("Selecciona un horario");
        cbHorarios.setPrefWidth(260);
        cbHorarios.setStyle(
            "-fx-background-color: #241a30; -fx-text-fill: #f4e8d0; " +
            "-fx-background-radius: 8; -fx-border-color: #3a2b4a; -fx-border-radius: 8;"
        );
        configurarCeldas();

        Label lblCantidadTitulo = new Label("CANTIDAD DE BOLETOS");
        lblCantidadTitulo.setStyle("-fx-font-size: 12px; -fx-text-fill: #b8a9c9; -fx-font-weight: bold; -fx-letter-spacing: 1px;");

        spCantidad = new Spinner<>();
        spCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        spCantidad.setEditable(true);
        spCantidad.setPrefWidth(100);
        spCantidad.setStyle("-fx-background-color: #241a30;");

        HBox filaHorarioCantidad = new HBox(30);
        VBox colHorario = new VBox(6, lblHorarioTitulo, cbHorarios);
        VBox colCantidad = new VBox(6, lblCantidadTitulo, spCantidad);
        filaHorarioCantidad.getChildren().addAll(colHorario, colCantidad);

        btnComprar = new Button("🎟 Comprar boleto");
        btnComprar.setMaxWidth(Double.MAX_VALUE);
        String btnNormal = "-fx-background-color: #d4af37; -fx-text-fill: #1b1224; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 10 0 10 0; -fx-cursor: hand;";
        String btnHover = "-fx-background-color: #e6c34f; -fx-text-fill: #1b1224; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 10 0 10 0; -fx-cursor: hand;";
        btnComprar.setStyle(btnNormal);
        btnComprar.setOnMouseEntered(e -> btnComprar.setStyle(btnHover));
        btnComprar.setOnMouseExited(e -> btnComprar.setStyle(btnNormal));

        VBox columnaInfo = new VBox(14, lblTitulo, lblInfo, lblSinopsisTitulo, lblSinopsis, filaHorarioCantidad);
        columnaInfo.setPadding(new Insets(0, 0, 0, 24));
        VBox.setVgrow(columnaInfo, Priority.ALWAYS);

        HBox filaContenido = new HBox(imgPoster, columnaInfo);
        filaContenido.setPadding(new Insets(20, 30, 20, 30));

        Region espaciador = new Region();
        VBox.setVgrow(espaciador, Priority.ALWAYS);

        VBox contenedorBoton = new VBox(btnComprar);
        contenedorBoton.setPadding(new Insets(0, 30, 30, 30));

        this.getChildren().addAll(barraSuperior, filaContenido, espaciador, contenedorBoton);
    }

    private void configurarCeldas() {
        Callback<javafx.scene.control.ListView<FuncionDTO>, ListCell<FuncionDTO>> factory =
            lv -> new ListCell<>() {
                @Override
                protected void updateItem(FuncionDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : formatearFuncion(item));
                }
            };
        cbHorarios.setCellFactory(factory);
        cbHorarios.setButtonCell(factory.call(null));
    }

    private String formatearFuncion(FuncionDTO f) {
        return f.getHorario().format(FORMATO_HORA) + " — " + f.getNombreSala()
                + " — $" + f.getPrecioBase() + " (" + f.getAsientosDisponibles() + " disp.)";
    }

    public void cargarPelicula(Pelicula pelicula) {
        lblTitulo.setText(pelicula.getTitulo());
        lblInfo.setText(pelicula.getGenero() + " • " + pelicula.getDuracionMinutos() + " min");
        lblSinopsis.setText(
            pelicula.getSinopsis() != null && !pelicula.getSinopsis().isBlank()
                ? pelicula.getSinopsis() : "Sinopsis no disponible."
        );
        try {
            imgPoster.setImage(new Image(pelicula.getRutaPoster(), true));
        } catch (Exception e) {
            // Se deja el rectángulo con el color de fondo definido en el estilo
        }
    }

    public void cargarHorarios(List<FuncionDTO> funciones) {
        cbHorarios.getItems().setAll(funciones);
    }

    public ComboBox<FuncionDTO> getCbHorarios() { return cbHorarios; }
    public Spinner<Integer> getSpCantidad() { return spCantidad; }
    public Button getBtnComprar() { return btnComprar; }
    public Button getBtnCerrar() { return btnCerrar; }
}