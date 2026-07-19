package com.bmw.cine.personal.controller;

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
import com.bmw.cine.common.util.Notificador;
import com.bmw.cine.personal.view.EmitirBoletoView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

public class EmitirBoletoController {

    private final EmitirBoletoView vista;
    private final UsuarioDTO usuarioActivo;

    private final BoletoDAO boletoDAO = new BoletoDAOImpl();
    private final PeliculaDAO peliculaDAO = new PeliculaDAOImpl();
    private final FuncionDAO funcionDAO = new FuncionDAOImpl();
    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    private UsuarioDTO usuarioSeleccionado;

    public EmitirBoletoController(EmitirBoletoView vista, UsuarioDTO usuarioActivo) {
        this.vista = vista;
        this.usuarioActivo = usuarioActivo;

        configurarBusquedaUsuario();
        configurarPeliculaYFuncion();
        configurarBotonEmitir();
    }

    // Usuario
    private void configurarBusquedaUsuario() {
        vista.getListaUsuarios().setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(UsuarioDTO u, boolean empty) {
                super.updateItem(u, empty);
                setText(empty || u == null ? null : u.getNombre() + " — " + u.getCorreo());
            }
        });

        vista.getListaUsuarios().getSelectionModel().selectedItemProperty().addListener((obs, anterior, nuevo) -> {
            usuarioSeleccionado = nuevo;
            vista.getLabelUsuarioSeleccionado().setText(nuevo == null
                    ? "Ningún usuario seleccionado"
                    : "Seleccionado: " + nuevo.getNombre() + " (" + nuevo.getCorreo() + ")");
        });

        vista.getBtnBuscar().setOnAction(e -> buscarUsuarios());
        vista.getCampoBusquedaUsuario().setOnAction(e -> buscarUsuarios());
    }

    private void buscarUsuarios() {
        String texto = vista.getCampoBusquedaUsuario().getText();
        if (texto == null || texto.isBlank()) {
            vista.getListaUsuarios().setItems(FXCollections.observableArrayList());
            return;
        }
        try {
            List<UsuarioDTO> resultados = usuarioDAO.buscarPorTexto(texto.trim());
            vista.getListaUsuarios().setItems(FXCollections.observableArrayList(resultados));
            if (resultados.isEmpty()) {
                vista.getListaUsuarios().setPlaceholder(new Label("Sin resultados para \"" + texto + "\""));
            }
        } catch (DAOException e) {
            Notificador.error("Error", "Error al buscar usuarios: " + e.getMessage());
        }
    }

    // Película / función
    private void configurarPeliculaYFuncion() {
        List<Pelicula> peliculas = peliculaDAO.listarTodas();
        vista.getComboPelicula().setItems(FXCollections.observableArrayList(peliculas));
        vista.getComboPelicula().setConverter(new StringConverter<>() {
            @Override public String toString(Pelicula p) { return p == null ? "" : p.getTitulo(); }
            @Override public Pelicula fromString(String s) { return null; }
        });
        vista.getComboPelicula().setOnAction(e -> actualizarComboFuncion(vista.getComboPelicula().getValue()));

        vista.getComboFuncion().setConverter(new StringConverter<>() {
            @Override public String toString(FuncionDTO f) {
                return f == null ? "" : f.getHorario() + " · " + f.getNombreSala();
            }
            @Override public FuncionDTO fromString(String s) { return null; }
        });
        vista.getComboFuncion().setOnAction(e -> cargarAsientosDisponibles());
    }

    private void actualizarComboFuncion(Pelicula peliculaSeleccionada) {
        ObservableList<FuncionDTO> opciones = FXCollections.observableArrayList();
        if (peliculaSeleccionada != null) {
            opciones.addAll(funcionDAO.listarPorPelicula(peliculaSeleccionada.getId()));
        }
        vista.getComboFuncion().setItems(opciones);
        vista.getComboFuncion().setValue(null);
        vista.getListaAsientos().setItems(FXCollections.observableArrayList());
        vista.getLabelAsientosInfo().setText("");
    }

    // Asientos
    private void cargarAsientosDisponibles() {
        FuncionDTO funcion = vista.getComboFuncion().getValue();
        if (funcion == null) {
            vista.getListaAsientos().setItems(FXCollections.observableArrayList());
            return;
        }
        try {
            int[] dimensiones = funcionDAO.obtenerDimensionesSala(funcion.getFuncionId());
            List<String> ocupados = boletoDAO.listarAsientosOcupados(funcion.getFuncionId());
            List<String> disponibles = generarAsientosDisponibles(dimensiones[0], dimensiones[1], ocupados);

            vista.getListaAsientos().setItems(FXCollections.observableArrayList(disponibles));
            vista.getLabelAsientosInfo().setText(disponibles.size() + " asiento(s) disponible(s) de "
                    + (dimensiones[0] * dimensiones[1]) + " totales.");
        } catch (DAOException e) {
            Notificador.error("Error", "Error al cargar asientos: " + e.getMessage());
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

    // Emisión
    private void configurarBotonEmitir() {
        vista.getBtnEmitir().setOnAction(e -> emitir());
    }

    private void emitir() {
        if (usuarioSeleccionado == null) {
            Notificador.advertencia("Datos incompletos", "Seleccioná un espectador primero.");
            return;
        }
        FuncionDTO funcion = vista.getComboFuncion().getValue();
        if (funcion == null) {
            Notificador.advertencia("Datos incompletos", "Seleccioná una función.");
            return;
        }
        List<String> asientosElegidos = new ArrayList<>(vista.getListaAsientos().getSelectionModel().getSelectedItems());
        if (asientosElegidos.isEmpty()) {
            Notificador.advertencia("Datos incompletos", "Seleccioná al menos un asiento.");
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
            Notificador.advertencia("Emisión parcial",
                    "No se pudieron emitir estos asientos (probablemente ya ocupados): " + String.join(", ", fallidos)
                            + (emitidos.isEmpty() ? "" : "\nSí se emitieron: " + String.join(", ", emitidos)));
        } else {
            Notificador.exito("Boleto(s) emitido(s) correctamente: " + String.join(", ", emitidos));
            vista.getCampoBusquedaUsuario().clear();
            vista.getListaUsuarios().setItems(FXCollections.observableArrayList());
            usuarioSeleccionado = null;
            vista.getLabelUsuarioSeleccionado().setText("Ningún usuario seleccionado");
        }
    }
}