package com.bmw.cine.common.dao;

import com.bmw.cine.common.dto.PeliculaCardDTO;
import com.bmw.cine.common.model.Pelicula;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de acceso a datos para Pelicula. Compartido por 3 flujos:
 *  - Espectador: solo lectura (cartelera + detalle).
 *  - Personal: CRUD completo (Meta "CRUD de Cartelera").
 *  - Administrador: solo lectura indirecta (a través de reportes).
 */

public interface PeliculaDAO {
    /**
     * Solo películas activas, en formato DTO liviano para pintar tarjetas.
     * Usado por la vista de Cartelera (Espectador, y Personal/Admin cuando
     * entran a Cartelera desde el Selector de Módulo).
     */
    List<PeliculaCardDTO> listarCartelera();
    Optional<Pelicula> buscarPorId(int id); // Entidad completa, para la vista de Detalle de Película.
    List<Pelicula> listarTodas(); // Incluye inactivas — para el CRUD de Personal, no para Cartelera.
    Pelicula crear(Pelicula pelicula);
    boolean actualizar(Pelicula pelicula);
    boolean actualizarVisibilidad(int id, boolean visible); // (activa=True)(desactivada=False)
}
