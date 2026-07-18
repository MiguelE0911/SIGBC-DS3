package com.bmw.cine.espectador.controller;

import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.espectador.model.Boleto;
import com.bmw.cine.espectador.view.BilleteraView;
import com.bmw.cine.espectador.view.BoletoItemView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import java.util.List;

/**
 * Controlador para gestionar la lógica de la Billetera del Espectador.
 * Se encarga de solicitar los boletos al DAO y poblar la vista.
 * 
 * @author Wilma
 * @version 1.0
 */
public class BilleteraController {

    private final BilleteraView vista;
    private final UsuarioDTO usuarioActivo;
    // Nota: Aquí usarías un BoletoDAO. Si aún no lo creas, puedes 
    // centralizar la consulta en una nueva interfaz o en UsuarioDAO.
    // private final BoletoDAO boletoDAO; 

    /**
     * Constructor del controlador.
     * 
     * @param vista         La instancia de la BilleteraView (Panel lateral).
     * @param usuarioActivo Datos del usuario que inició sesión.
     */
    public BilleteraController(BilleteraView vista, UsuarioDTO usuarioActivo) {
        this.vista = vista;
        this.usuarioActivo = usuarioActivo;
        // this.boletoDAO = new BoletoDAOImpl(); 
        
        inicializarBilletera();
    }

    /**
     * Carga inicial de datos al abrir el panel.
     */
    private void inicializarBilletera() {
        actualizarListaBoletos();
    }

    /**
     * Solicita los datos a la capa DAO y renderiza cada boleto en la vista.
     */
    public void actualizarListaBoletos() {
        // Limpiar la lista actual antes de recargar
        vista.getContenedorBoletos().getChildren().clear();

        try {
            // TODO: Implementar boletoDAO.listarPorUsuario(usuarioActivo.getId())
            // Simulamos la obtención de datos para la Meta 3:
            List<Boleto> boletos = obtenerBoletosSimulados(); 

            if (boletos.isEmpty()) {
                mostrarMensajeVacio();
            } else {
                for (Boleto boleto : boletos) {
                    // Creamos el componente visual individual
                    BoletoItemView itemView = new BoletoItemView(boleto);
                    
                    // Agregamos la "tarjeta" al VBox de la BilleteraView
                    vista.getContenedorBoletos().getChildren().add(itemView);
                }
            }
        } catch (DAOException e) {
            mostrarAlertaError("Error de Carga", "No se pudieron obtener tus boletos.");
            e.printStackTrace();
        }
    }

    private void mostrarMensajeVacio() {
        javafx.scene.control.Label lblVacio = new javafx.scene.control.Label("No tienes boletos comprados.");
        lblVacio.setStyle("-fx-text-fill: #b8a9c9; -fx-font-style: italic;");
        vista.getContenedorBoletos().getChildren().add(lblVacio);
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Simulación temporal para pruebas visuales de la Meta 3.
     */
    private List<Boleto> obtenerBoletosSimulados() {
        return List.of(
            new Boleto(101, "Batman", "A1", Boleto.EstadoBoleto.CONFIRMADO, java.time.LocalDateTime.now()),
            new Boleto(102, "Inception", "B4", Boleto.EstadoBoleto.PENDIENTE, java.time.LocalDateTime.now().plusDays(1))
        );
    }
}