package com.bmw.cine.common.dao;

import com.bmw.cine.common.dto.FuncionDTO;
import com.bmw.cine.common.model.Funcion;

import java.util.List;
import java.util.Optional;

public interface FuncionDAO {
    /**
     * Horarios disponibles de una película, ya con nombre de sala y
     * asientos disponibles resueltos — usado en Detalle de Película
     * (Espectador) para elegir función.
     */
    List<FuncionDTO> listarPorPelicula(int peliculaId);

    Optional<Funcion> buscarPorId(int id);

    // Para la vista de Selección de asiento: qué códigos ya están ocupados.
    List<String> listarAsientosOcupados(int funcionId);

    // Gestión de horarios (Personal).
    List<Funcion> listarTodas();

    Funcion crear(Funcion funcion);

    boolean actualizar(Funcion funcion);

    boolean eliminar(int id);
}
