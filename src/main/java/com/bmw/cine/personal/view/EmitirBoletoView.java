package com.bmw.cine.personal.view;

import com.bmw.cine.common.dao.BoletoDAO;
import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dao.FuncionDAO;
import com.bmw.cine.common.dao.PeliculaDAO;
import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dao.impl.BoletoDAOImpl;
import com.bmw.cine.common.dao.impl.FuncionDAOImpl;
import com.bmw.cine.common.dao.impl.PeliculaDAOImpl;
import com.bmw.cine.common.dao.impl.UsuarioDAOImpl;
import com.bmw.cine.common.dto.FuncionDTO;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Pelicula;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Emisión manual de boletos por parte de Personal/Administrador.
 * El boleto se crea directamente como CONFIRMADO.
 *
 * NOTA: la selección de asiento usa un ListView de códigos generados a
 * partir de filas/columnas de la sala (A1, A2, ...) como reemplazo
 * temporal del mapa de asientos visual del Espectador. Cuando ese mapa
 * exista, esta pantalla debería reutilizarlo en vez de este ListView.
 */
public class EmitirBoletoView extends BorderPane {

    private final BoletoDAO boletoDAO = new BoletoDAOImpl();
    private final PeliculaDAO peliculaDAO = new PeliculaDAOImpl();
    private final FuncionDAO funcionDAO = new FuncionDAOImpl();
    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    private final UsuarioDTO usuarioActivo;

    // --- Búsqueda de usuario ---
    private final TextField campoBusquedaUsuario = new TextField();
    private final ListView<UsuarioDTO> listaUsuarios = new ListView<>();
    private UsuarioDTO usuarioSeleccionado;
    private final Label labelUsuarioSeleccionado = new Label("Ningún usuario seleccionado");

    // --- Película / función ---
    private final ComboBox<Pelicula> comboPelicula = new ComboBox<>();
    private final ComboBox<FuncionDTO> comboFuncion = new ComboBox<>();

    // --- Asientos (placeholder del mapa real) ---
    private final ListView<String> listaAsientos = new ListView<>();
    private final Label labelAsientosInfo = new Label();

    private final Button btnEmitir = new Button("Emitir boleto(s)");

    public EmitirBoletoView(UsuarioDTO usuarioActivo) {
        this.usuarioActivo = usuarioActivo;

        getStyleClass().add("vista-cuerpo");
        setPadding(new Insets(24));

        setTop(construirEncabezado());
        setCenter(construirFormulario());

        configurarBusquedaUsuario();
        configurarPeliculaYFuncion();
        configurarListaAsientos();
        configurarBotonEmitir();
    }

    private VBox construirEncabezado() {
        Label titulo = new Label("Emisión manual de boletos");
        titulo.getStyleClass().add("vista-titulo");
        VBox encabezado = new VBox(8, titulo);
        encabezado.setPadding(new Insets(0, 0, 16, 0));
        return encabezado;
    }

    private VBox construirFormulario() {
        // --- Sección usuario ---
        campoBusquedaUsuario.setPromptText("Buscar por nombre, correo o username...");
        Button btnBuscar = new Button("Buscar");
        btnBuscar.getStyleClass().add("boton-secundario");
        HBox filaBusqueda = new HBox(8, campoBusquedaUsuario, btnBuscar);
        filaBusqueda.setAlignment(Pos.CENTER_LEFT);

        listaUsuarios.setPrefHeight(120);
        listaUsuarios.setPlaceholder(new Label("Buscá un usuario para ver resultados"));

        labelUsuarioSeleccionado.getStyleClass().add("vista-subtitulo");

        VBox seccionUsuario = new VBox(8,
                new Label("1. Espectador"), filaBusqueda, listaUsuarios, labelUsuarioSeleccionado);

        btnBuscar.setOnAction(e -> buscarUsuarios());
        campoBusquedaUsuario.setOnAction(e -> buscarUsuarios());

        // --- Sección película/función ---
        comboPelicula.setPromptText("Seleccionar película");
        comboFuncion.setPromptText("Seleccionar función");
        HBox filaPeliculaFuncion = new HBox(12, comboPelicula, comboFuncion);
        filaPeliculaFuncion.setAlignment(Pos.CENTER_LEFT);

        VBox seccionFuncion = new VBox(8,
                new Label("2. Película y función"), filaPeliculaFuncion);

        // --- Sección asientos ---
        listaAsientos.setPrefHeight(160);
        listaAsientos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listaAsientos.setPlaceholder(new Label("Elegí una función primero"));
        labelAsientosInfo.getStyleClass().add("vista-subtitulo");

        VBox seccionAsientos = new VBox(8,
                new Label("3. Asiento(s) — selección múltiple"), listaAsientos, labelAsientosInfo);

        // --- Botón emitir ---
        btnEmitir.getStyleClass().add("boton-aprobar");

        VBox contenedor = new VBox(24, seccionUsuario, new Separator(), seccionFuncion,
                new Separator(), seccionAsientos, btnEmitir);
        contenedor.setMaxWidth(520);
        return contenedor;
    }

    // ---------- Usuario ----------

    private void configurarBusquedaUsuario() {
        listaUsuarios.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(UsuarioDTO u, boolean empty) {
                super.updateItem(u, empty);
                setText(empty || u == null ? null : u.getNombre() + " — " + u.getCorreo());
            }
        });

        listaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nuevo) -> {
            usuarioSeleccionado = nuevo;
            labelUsuarioSeleccionado.setText(nuevo == null
                    ? "Ningún usuario seleccionado"
                    : "Seleccionado: " + nuevo.getNombre() + " (" + nuevo.getCorreo() + ")");
        });
    }

    private void buscarUsuarios() {
        String texto = campoBusquedaUsuario.getText();
        if (texto == null || texto.isBlank()) {
            listaUsuarios.setItems(FXCollections.observableArrayList());
            return;
        }
        try {
            List<UsuarioDTO> resultados = usuarioDAO.buscarPorTexto(texto.trim());
            listaUsuarios.setItems(FXCollections.observableArrayList(resultados));
            if (resultados.isEmpty()) {
                listaUsuarios.setPlaceholder(new Label("Sin resultados para \"" + texto + "\""));
            }
        } catch (DAOException e) {
            mostrarError("Error al buscar usuarios: " + e.getMessage());
        }
    }

    // ---------- Película / función ----------

    private void configurarPeliculaYFuncion() {
        List<Pelicula> peliculas = peliculaDAO.listarTodas();
        comboPelicula.setItems(FXCollections.observableArrayList(peliculas));
        comboPelicula.setConverter(new StringConverter<>() {
            @Override public String toString(Pelicula p) { return p == null ? "" : p.getTitulo(); }
            @Override public Pelicula fromString(String s) { return null; }
        });
        comboPelicula.setOnAction(e -> actualizarComboFuncion(comboPelicula.getValue()));

        comboFuncion.setConverter(new StringConverter<>() {
            @Override public String toString(FuncionDTO f) {
                return f == null ? "" : f.getHorario() + " · " + f.getNombreSala();
            }
            @Override public FuncionDTO fromString(String s) { return null; }
        });
        comboFuncion.setOnAction(e -> cargarAsientosDisponibles());
    }

    private void actualizarComboFuncion(Pelicula peliculaSeleccionada) {
        ObservableList<FuncionDTO> opciones = FXCollections.observableArrayList();
        if (peliculaSeleccionada != null) {
            opciones.addAll(funcionDAO.listarPorPelicula(peliculaSeleccionada.getId()));
        }
        comboFuncion.setItems(opciones);
        comboFuncion.setValue(null);
        listaAsientos.setItems(FXCollections.observableArrayList());
        labelAsientosInfo.setText("");
    }

    // ---------- Asientos (placeholder) ----------

    private void configurarListaAsientos() {
        // sin configuración adicional por ahora; queda listo para swap
        // por el mapa visual real del Espectador más adelante.
    }

    private void cargarAsientosDisponibles() {
        FuncionDTO funcion = comboFuncion.getValue();
        if (funcion == null) {
            listaAsientos.setItems(FXCollections.observableArrayList());
            return;
        }
        try {
            int[] dimensiones = funcionDAO.obtenerDimensionesSala(funcion.getFuncionId());
            List<String> ocupados = boletoDAO.listarAsientosOcupados(funcion.getFuncionId());
            List<String> disponibles = generarAsientosDisponibles(dimensiones[0], dimensiones[1], ocupados);

            listaAsientos.setItems(FXCollections.observableArrayList(disponibles));
            labelAsientosInfo.setText(disponibles.size() + " asiento(s) disponible(s) de "
                    + (dimensiones[0] * dimensiones[1]) + " totales.");
        } catch (DAOException e) {
            mostrarError("Error al cargar asientos: " + e.getMessage());
        }
    }

    private List<String> generarAsientosDisponibles(int filas, int columnas, List<String> ocupados) {
        List<String> disponibles = new ArrayList<>();
        for (int f = 0; f < filas; f++) {
            char letraFila = (char) ('A' + f);
            for (int c = 1; c <= columnas; c++) {
                String codigo = letraFila + String.valueOf(c);
                if (!ocupados.contains(codigo)) disponibles.add(codigo);
            }
        }
        return disponibles;
    }

    // ---------- Emisión ----------

    private void configurarBotonEmitir() {
        btnEmitir.setOnAction(e -> emitir());
    }

    private void emitir() {
        if (usuarioSeleccionado == null) {
            mostrarError("Seleccioná un espectador primero.");
            return;
        }
        FuncionDTO funcion = comboFuncion.getValue();
        if (funcion == null) {
            mostrarError("Seleccioná una función.");
            return;
        }
        List<String> asientosElegidos = new ArrayList<>(listaAsientos.getSelectionModel().getSelectedItems());
        if (asientosElegidos.isEmpty()) {
            mostrarError("Seleccioná al menos un asiento.");
            return;
        }

        List<String> emitidos = new ArrayList<>();
        List<String> fallidos = new ArrayList<>();

        for (String asiento : asientosElegidos) {
            try {
                boletoDAO.emitirConfirmado(usuarioSeleccionado.getId(), funcion.getFuncionId(),
                        asiento, usuarioActivo.getId());
                emitidos.add(asiento);
            } catch (DAOException e) {
                // Asiento tomado justo ahora (choque de concurrencia) o cualquier otro
                // error de BD: en ambos casos no se pudo emitir ese asiento puntual.
                fallidos.add(asiento);
            }
        }

        // Siempre refrescar disponibilidad después de intentar emitir,
        // sin importar si hubo fallos, para reflejar el estado real de la BD.
        cargarAsientosDisponibles();

        if (!fallidos.isEmpty()) {
            mostrarError("No se pudieron emitir estos asientos (probablemente ya ocupados): " + String.join(", ", fallidos)
                    + (emitidos.isEmpty() ? "" : "\nSí se emitieron: " + String.join(", ", emitidos)));
        } else {
            mostrarInfo("Boleto(s) emitido(s) correctamente: " + String.join(", ", emitidos));
            campoBusquedaUsuario.clear();
            listaUsuarios.setItems(FXCollections.observableArrayList());
            usuarioSeleccionado = null;
            labelUsuarioSeleccionado.setText("Ningún usuario seleccionado");
        }
    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}