package com.bmw.cine.common.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bmw.cine.common.dao.PeliculaDAO;
import com.bmw.cine.common.dto.PeliculaCardDTO;
import com.bmw.cine.common.model.Pelicula;

/**
 * Implementación de acceso a datos para Películas.
 * Cumple con la regla de oro: solo se menciona 'Impl' aquí [3, 4].
 */
public class PeliculaDAOImpl implements PeliculaDAO {

    @Override
    public List<PeliculaCardDTO> listarCartelera() {
        // Simulación temporal de datos para la Meta 4
        List<PeliculaCardDTO> lista = new ArrayList<>();
        lista.add(new PeliculaCardDTO(1, "Inception", "https://via.placeholder.com/170x250", "Sci-Fi", 148));
        lista.add(new PeliculaCardDTO(2, "The Dark Knight", "https://via.placeholder.com/170x250", "Acción", 152));
        lista.add(new PeliculaCardDTO(3, "Interstellar", "https://via.placeholder.com/170x250", "Drama", 169));
        return lista;
    }

    @Override
    public Optional<Pelicula> buscarPorId(int id) {
        return Optional.empty();
    }

    @Override
    public List<Pelicula> listarTodas() {
        return new ArrayList<>();
    }

    @Override
    public Pelicula crear(Pelicula pelicula) {
        return pelicula;
    }

    @Override
    public boolean actualizar(Pelicula pelicula) {
        return true;
    }

    @Override
    public boolean actualizarVisibilidad(int id, boolean visible) {
        return true;
    }
}