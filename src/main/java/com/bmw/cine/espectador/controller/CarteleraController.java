package com.bmw.cine.espectador.controller;

import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dao.PeliculaDAO;
import com.bmw.cine.common.dao.impl.PeliculaDAOImpl;
import com.bmw.cine.common.dto.PeliculaCardDTO;
import com.bmw.cine.espectador.view.CarteleraView;
import com.bmw.cine.espectador.view.PeliculaCardView;
import java.util.List;

public class CarteleraController {
    private final CarteleraView vista;
    private final PeliculaDAO peliculaDAO;

    public CarteleraController(CarteleraView vista) {
        this.vista = vista;
        this.peliculaDAO = new PeliculaDAOImpl(); // Regla: Solo aquí se usa "Impl" [8]
        cargarCartelera();
    }

    private void cargarCartelera() {
        try {
            // Obtiene solo películas con 'activa = true' del DAO [2]
            List<PeliculaCardDTO> listaPeliculas = peliculaDAO.listarCartelera();
            
            vista.getFlowPaneCartelera().getChildren().clear();
            for (PeliculaCardDTO dto : listaPeliculas) {
                PeliculaCardView card = new PeliculaCardView(dto);
                
                // Evento para Meta 5: Detalle de película
                card.setOnMouseClicked(e -> System.out.println("Cargando detalle de: " + dto.getTitulo()));
                
                vista.getFlowPaneCartelera().getChildren().add(card);
            }
        } catch (DAOException e) {
            // Manejo de error según guía [9]
            System.err.println("Error al cargar cartelera: " + e.getMessage());
        }
    }
}