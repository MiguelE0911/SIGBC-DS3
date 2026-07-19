package com.bmw.cine.espectador.controller;

import java.util.List;

import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.espectador.model.Boleto;
import com.bmw.cine.espectador.view.BilleteraView;
import com.bmw.cine.espectador.view.BoletoItemView;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * Controlador para gestionar la lógica de la Billetera del Espectador.
 * Se encarga de solicitar los boletos al DAO y poblar la vista.
 * 
 * @author Wilma
 * @version 1.1
 */
public class BilleteraController {

    private final BilleteraView vista;
    private final UsuarioDTO usuarioActivo;

    /**
     * Constructor del controlador.
     * 
     * @param vista         La instancia de la BilleteraView (Panel lateral).
     * @param usuarioActivo Datos del usuario que inició sesión.
     */
    public BilleteraController(BilleteraView vista, UsuarioDTO usuarioActivo) {
        this.vista = vista;
        this.usuarioActivo = usuarioActivo;
        
        // SOLUCIÓN AL AVISO: Se utiliza 'usuarioActivo' para personalizar la vista
        // de la billetera al momento de iniciarla.
        Label lblUsuario = new Label("Billetera de: " + usuarioActivo.getNombre());
        lblUsuario.setStyle("-fx-text-fill: #f4e8d0; -fx-font-weight: bold; -fx-font-size: 14px;");
        this.vista.getContenedorBoletos().getChildren().add(lblUsuario);
        
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
    @SuppressWarnings("CallToPrintStackTrace")
    public void actualizarListaBoletos() {
        // Limpiamos los boletos anteriores, manteniendo el encabezado si es necesario
        // En este caso, removemos solo los items de tipo BoletoItemView para no borrar el nombre arriba
        vista.getContenedorBoletos().getChildren().removeIf(node -> node instanceof BoletoItemView);

        try {
            // TODO: En la fase funcional se reemplazará por la consulta real:
            // List<Boleto> boletos = boletoDAO.listarPorUsuario(usuarioActivo.getId());
            
            // Simulamos la obtención de datos para las pruebas de la Meta 3
            List<Boleto> boletos = obtenerBoletosSimulados(); 

            if (boletos.isEmpty()) {
                mostrarMensajeVacio();
            } else {
                for (Boleto boleto : boletos) {
                    // Creamos el componente visual individual (tarjeta)
                    BoletoItemView itemView = new BoletoItemView(boleto);
                    
                    // Agregamos la "tarjeta" al contenedor scrolleable de la vista
                    vista.getContenedorBoletos().getChildren().add(itemView);
                }
            }
        } catch (DAOException e) {
            mostrarAlertaError("Error de Carga", "No se pudieron obtener tus boletos.");
            e.printStackTrace();
        }
    }

    private void mostrarMensajeVacio() {
        Label lblVacio = new Label("No tienes boletos comprados.");
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